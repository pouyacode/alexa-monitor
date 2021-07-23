;;; Here we handle everything relative to crawling the webpage and extracting
;;; the parts we need. Let's choose cool function names; If it's about getting
;;; some html page, why not call it `web-grab`?!
;;;
;;; Here we need 3 different things from the webpage, so we have one function
;;; for each! They look a lot alike, but I decided not to mix them into one
;;; function, so the result would be easier to read and maintain.
;;; Maybe later we can find a way to remove duplication without making it look
;;; messy.
(ns alexa-monitor.collector
  (:require [hato.client :as web]
            [hickory.core :as hick]
            [hickory.select :as s])
  (:gen-class))


;;; It returns `java.lang.Long`.
;;; I used an `or` to return `0` if there wasn't any digit in the string.
;;; Because java's `Long` can be `nil` sometimes!
(defn digitize
  "Trim string and return its digit part."
  [string]
  (try
    (or
     (->> string
          (re-seq #"[\d]+")
          clojure.string/join
          clojure.edn/read-string)
     0)
    (catch Exception e nil)))


;;; Creates a simple HTTP GET request, ignores the status and only returns the
;;; HTML part.
;;;
;;; The url is hard-coded here. You need to run a simple webserver to serve
;;; contents of `resources` directory on port 8000.
;;; I use `python3 -m http.server 8000` for development.
;;;
;;; You need to change this url to `https://www.alexa.com/minisiteinfo/`
;;; If you want to create actual requests to alexa.
;;;
;;; TODO: Error handling.
(defn web-grab
  "Retrieve the page and returns html output."
  [url]
  (println "Getting updates for: " url)
  (try (:body
        #_(web/get (str "http://localhost:8000/" url "/"))
        (web/get (str "https://www.alexa.com/minisiteinfo/" url)))
       (catch Exception e "")))


;;; Let's have 'hickory' handle everything.
(defn hiccupize
  "Generate hiccup from html input."
  [html]
  (-> html
      hick/parse
      hick/as-hickory))


;;; Extract the rank of our domain from the webpage and `gigitize` it.
;;; Then add it to `domain-map` hash-map and return the result.
(defn rank
  "Proces the hiccup and read `Alexa Rank`"
  [domain-map hiccup]
  (-> (s/select (s/child (s/class "nopaddingbottom")
                         (s/tag :div)
                         (s/tag :a))
                hiccup)
      first
      :content
      second
      digitize
      (#(conj domain-map [:rank %]))))


;;; Extract the backlink count, then `digitize` it.
;;; Then add it to `domain-map` hash-map and return the result.
(defn backlink
  "Process the hiccup and read `Sites Linking In`."
  [domain-map hiccup]
  (-> (s/select (s/child (s/class "nopaddingbottom")
                         (s/tag :div)
                         (s/class "nomargin")
                         (s/tag :a))
                hiccup)
      first
      :content
      first
      digitize
      (#(conj domain-map [:backlink %]))))


;;; Creates a nice data-flow through every function of this namespace.
;;; First request the url and create a hicuup its HTML elements, Then extract
;;; `rank` and `backlink` from it.
(defn main
  "Entry point."
  [domain-map]
  (let [hiccup (-> :domain
                   domain-map
                   web-grab
                   hiccupize)]
    (-> domain-map
        (rank hiccup)
        (backlink hiccup))))
