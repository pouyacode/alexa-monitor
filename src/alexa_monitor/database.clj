;;; Small namespace with few useful functions to work with database.
(ns alexa-monitor.database
  (:require [clojure.java.jdbc :refer :all])
  (:gen-class))


;;; We currently use sqlite. Since it's a small project, even a CSV file could do the trick.
;;; But let's keep things professional!
;;; MySQL or similar database would eliminate some troubles we have with `java.io.File` and its weird behavior
;;; on different stages of our project (repl, uberjar, native-image), but we'll try to make it work.
;;; If we couldn't go on, we'll switch to MySQL (or preferably PostgrSQL)
(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname (.getAbsolutePath (new java.io.File "db/database.db"))})


;;; For now, we create the `db` directory and run this function manually (in repl) when we need to create database.
;;; Check out [this (beginner friendly) issue](https://github.com/pouyacode/alexa-monitor/issues/2) if you want to write error handling
;;; for this part and create directory and database automatically if the software couldn't find database.
(defn create-db
  "Create database and table."
  []
  (try (db-do-commands db
                       (create-table-ddl :rank
                                         [[:id :integer :primary :key :asc]
                                          [:site :text]
                                          [:rank :integer]
                                          [:backlink :integer]
                                          [:timestamp :datetime :default :current_timestamp]]))
       (catch Exception e
         (println (.getMessage e)))))


(defn last-rank
  "Get the last recorded rank for given domain name. It returns a lazy sequence."
  [site]
  (-> db
      (query [(str "SELECT rank FROM rank where site='" site "' order by id desc limit 1")])
      first
      :rank))


(defn new-entry
  "Wrapper around jdbc/insert!"
  [db table data]
  (insert! db table data))


#_(create-db)
