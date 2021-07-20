;;; Obviously, the entry point of our application.
;;; Here we import `collector` and `database` namespace and call some functions
;;; within them.
(ns alexa-monitor.core
  (:require [alexa-monitor.collector :as collector]
            [alexa-monitor.database :as database]
            [overtone.at-at :as at])
  (:gen-class))


;;; This function calls `database/domain-list` and gets a list of current domains
;;; to watch. Each domain is inside a hash-map. Something like
;;; `{:domain_id 1, :domain "pouyacode.net"}` and sends it to `collector/main`
;;; to add more information to that hash-map.
;;;
;;; Then we `(dissoc :domain)` from it to make it ready for our `rank` table.
;;; and send the result to `database/new-entry` to be inserted into database.
(defn update-rank
  "Crawl the page and grab some useful data via `alexa-monitor.collector` and
  put those data in database, using `alexa-monitor.database`."
  [& args]
  (let [domains (database/domain-list)]
    (doall (map #(-> %
                     collector/main
                     (dissoc :domain)
                     database/new-entry)
                domains))))


(defn -main
  "Create a simple schedule and call `update-rank` every 5 minutes."
  [& args]
  (let [thread-pool (at/mk-pool)]
    (at/every 10000 update-rank thread-pool :desc "updating ranks")))
