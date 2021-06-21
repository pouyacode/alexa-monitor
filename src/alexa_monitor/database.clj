(ns alexa-monitor.database
  (:require [clojure.java.jdbc :refer :all])
  (:gen-class))


(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname (.getAbsolutePath (new java.io.File "db/database.db"))})


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


(defn print-result-set
  "Print the result set in tabular form."
  [result-set]
  (doseq [row result-set]
    (println row)))


(defn last-rank
  "Execute query and return lazy sequence."
  [site]
  (-> db
      (query [(str "SELECT rank FROM rank where site='" site "' order by id desc limit 1")])
      first
      :rank))


(defn new-entry
  "Wrapper around jdbc/insert!"
  [db table data]
  (db table data))


#_(create-db)
