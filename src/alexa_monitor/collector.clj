(ns alexa-monitor.collector
  (:require [hato.client :as web]
            [hickory.core :as hick]
            [hickory.select :as s])
  (:gen-class))


(defn digitize
  "Removes any non-digit from string.
  Returns `java.lang.Long`.
  I used `or` to return `0` if there wasn't any digit in the string.
  Because java's `Long` can be `nil` sometimes."
  [string]
  (or 
   (->> string
        (re-seq #"[\d.]+")
        clojure.string/join
        clojure.edn/read-string)
   0))


(defn web-grab
  "Retrieve the page and returns html output. `hiccup` of its `body` element."
  [url]
  (-> (web/get url)
      :body))


(defn hiccupize
  "Generate hiccup from html input."
  [html]
  (-> html
      hick/parse
      hick/as-hickory))


(defn rank
  "Proces the hiccup and read `Alexa Rank`"
  [hiccup]
  (-> (s/select (s/child (s/class "nopaddingbottom")
                         (s/tag :div)
                         (s/tag :a))
                hiccup)
      first
      :content
      second
      digitize))


(defn backlink
  "Process the hiccup and read `Sites Linking In`."
  [hiccup]
  (-> (s/select (s/child (s/class "nopaddingbottom")
                         (s/tag :div)
                         (s/class "nomargin")
                         (s/tag :a))
                hiccup)
      first
      :content
      first
      digitize))


(defn sitename
  "Get sitename from webpage, without `http` and other parts."
  [hiccup]
  (-> (s/select (s/child (s/class "truncation"))
                hiccup)
      first
      :content
      first))


(defn hash-mapize [[site rank bl]]
  {:site site
   :rank rank
   :backlink bl})


(defn main
  "Entry point."
  [url]
  (hash-mapize
   ((juxt sitename rank backlink)
    (-> url
        web-grab
        hiccupize))))


#_(main "http://localhost:8080")
#_(main "https://www.alexa.com/minisiteinfo/pouyacode.net")
