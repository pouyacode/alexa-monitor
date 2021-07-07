;;; Small namespace with few useful functions to work with database.
(ns alexa-monitor.database
  (:require [clojure.java.jdbc :refer :all])
  (:gen-class))


;;; We currently use sqlite. Since it's a small project, even a CSV file could
;;; do the trick. But let's keep things professional!
;;; MySQL or similar database would eliminate some troubles we have with
;;; `java.io.File` and its weird behavior on different stages of our project
;;; (repl, uberjar, native-image), but we'll try to make it work.
;;; If we couldn't go on, we'll switch to MySQL (or preferably PostgrSQL)
(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname (.getAbsolutePath (new java.io.File "db/database.db"))})


;;; For now, we create the `db` directory and run this function manually
;;; (in repl) when we need to create database. Check out
;;; [this (beginner friendly) issue](https://github.com/pouyacode/alexa-monitor/issues/2)
;;; if you want to write error handling for this part and create directory and
;;; database automatically if the software couldn't find database.
(defn create-db
  "Create database and table."
  []
  (try (db-do-commands
        db
        (create-table-ddl :rank
                          [[:id :integer :primary :key :asc]
                           [:domain_id :integer]
                           [:rank :integer]
                           [:backlink :integer]
                           [:timestamp :datetime :default :current_timestamp]
                           [:foreign :key "(domain_id)" :references :domains "(domain_id)"]]))
       (catch Exception e
         (println (.getMessage e))))
  (try (db-do-commands
        db
        (create-table-ddl :domains
                          [[:domain_id :integer :primary :key :asc]
                           [:domain :text]]))
       (catch Exception e
         (.getMessage e))))


(defn domain-list
  "Get list of tracked websites from `domains` table."
  []
  (-> db
      (query ["SELECT * from domains"])))


(defn last-rank
  "Get the last recorded rank for given domain name. It returns a lazy sequence."
  [domain-id]
  (-> db
      (query [(str "SELECT rank FROM rank where domain_id='" domain-id "' order by id desc limit 1")])
      first
      :rank))


(defn new-entry
  "Wrapper around jdbc/insert!"
  [domain-map]
  (let [domain (:domain_id domain-map)
        last-record (last-rank domain)]
    (if (not= last-record (:rank domain-map))
      (insert! db "rank" domain-map))))
