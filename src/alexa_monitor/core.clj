;;; Obviously, the entry point of our application.
;;; Here we import `collector` and `database` namespace and call some functions within them.
(ns alexa-monitor.core
  (:require [alexa-monitor.collector :as collector]
            [alexa-monitor.database :as database])
  (:gen-class))


;;; You can run a simple webserver using `python3 -m http.server 8888` inside `resources` directory.
;;; So that our tests and development won't create actual requests to Alexa's servers.
;;; For creating `uberjar` or testing the actual result, change the `http://localhost:8080` to `https://www.alexa.com/minisiteinfo/domain.com`.
;;; For now, it works with a single hard-coded url.
;;;
;;; This functions sends our url to `collector/main` and gets our last database record from `database/last-rank`.
;;; If our last record and the crawled data were not equal, it creates a new entry in database.
(defn -main
  "Crawl the page and grab some useful data via `alexa-monitor.collector` and put those data in database, using `alexa-monitor.database`."
  [& args]
  (let [info (-> "http://localhost:8080" ;Change this to `https://www.alexa.com/minisiteinfo/domain.com` to create actual requests.
                 collector/main)
        last-db (database/last-rank (:site info)) ;Use the "site name" inside the crawled page, to get latest data from local database.
        changed? (not= last-db (:rank info))] ;Check if newly crawled data is the same as local database's latest record.
    (if changed?
      (database/new-entry database/db   ;Submit new rank to local database.
                          :rank
                          info))))
