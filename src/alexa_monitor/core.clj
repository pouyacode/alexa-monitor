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
                                         [[:site :text]
                                          [:rank :int]
                                          [:backlink :int]
                                          [:timestamp :datetime :default :current_timestamp]]))
       (catch Exception e
         (println (.getMessage e)))))


(defn print-result-set
  "Print the result set in tabular form."
  [result-set]
  (doseq [row result-set]
    (println row)))


(defn output
  "Execute query and return lazy sequence."
  []
  (query db ["SELECT * FROM rank"]))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (insert! db
           :rank
           #_(-> "https://www.alexa.com/minisiteinfo/pouyacode.net"
               collector/main)
           (-> "http://localhost:8080"
               collector/main)))


#_(create-db)
#_(insert! db :rank testdata)
#_(print-result-set (output))
