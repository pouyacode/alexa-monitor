(ns alexa-monitor.core
  (:require [alexa-monitor.collector :as collector]
            [alexa-monitor.database :as database])
  (:gen-class))


(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (let [info (-> "http://localhost:8080"
                 collector/main)
        last-db (database/last-rank (:site info))
        changed? (not= last-db (:rank info))]
    (if changed?
      (database/new-entry database/db
                          :rank
                          info))))
