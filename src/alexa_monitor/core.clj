(ns alexa-monitor.core
  (:require [alexa-monitor.collector :as collector]
            [clojure.java.jdbc :refer :all])
  (:gen-class))


(def db
  {:classname "org.sqlite.JDBC"
   :subprotocol "sqlite"
   :subname "db/database.db"})


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


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [info (-> "http://localhost:8080"
                 collector/main)
        last-db (last-rank (:site info))
        changed? (not= last-db (:rank info))]
    (if changed?
      (insert! db
               :rank
               info))))


#_(create-db)
#_(insert! db :rank testdata)
