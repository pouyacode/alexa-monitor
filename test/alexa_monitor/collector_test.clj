(ns alexa-monitor.collector-test
  (:require [clojure.test :refer :all]
            [alexa-monitor.collector :refer :all]
            [clojure.test.check :as tc]
            [clojure.test.check.generators :as gen]
            [clojure.test.check.properties :as prop]))


(def html
  "<!DOCTYPE html>
<html lang=\"en\">
<head><title>Alexa - Browser Extension</title></head>
<body data-cache-prefix=\"http://localhost/alx-sa-c0d6d28c-1620928494\" id=\"Extension\" class=\"Extension  noWidget\">
  <header class=\"flex\">
    <div class=\"FourthFull\">
        <i class=\"fa fa-globe\" aria-hidden=\"true\"></i>
        <h2>
            <a class=\"white nounderline truncation\" href=\"/siteinfo/pouyacode.net\" target=\"_blank\" >pouyacode.net</a>
        </h2>
    </div>
    <a class=\"white nounderline\" href=\"/siteinfo/pouyacode.net\" target=\"_blank\" ><img src=\"/images/logos/logo-white.png\" style=\"height:20px;\"/></a>
  </header>
  <section class=\"flex nopaddingbottom\">
    <div style=\"width:65%;\">  
      <p class=\"textbig\">Alexa Rank</p>
        <a href=\"/siteinfo/pouyacode.net\" target=\"_blank\" class=\"big data\"><span class=\"hash\">#</span>9,397,083 </a>
        <hr>
        <p class=\"textbig nomargin\">Sites Linking In: <a href=\"/siteinfo/pouyacode.net\" target=\"_blank\" class=\"small data\">1</a></p>
    </div> 
    <div style=\"width:35%;\">  
      <p class=\"textbig\">Similar Sites</p>
      <p>No data</p>
    </div>
  </section>
</body>
</html>")


(deftest test-hiccupize
  (testing "Just simple test, I don't think we need more."
    (is (= :document (-> html
                         hiccupize
                         :type)))))


(deftest test-parse
  (testing "Rank test."
    (is (= 9397083 (-> html
                       hiccupize
                       (#(scrape {:what :rank} %))))))
  (testing "Backlink test."
    (is (= 1 (-> html
                 hiccupize
                 (#(scrape {:what :backlink} %)))))))


(deftest test-digitize
  (testing "123"
    (is (= 123 (digitize "123"))))
  (testing "123,456"
    (is (= 123456 (digitize "123,456"))))
  (testing "Empty"
    (is (= 0 (digitize "Anything"))))
  (testing "Not trimmed"
    (is (= 42 (digitize "\n\t 42 \r\n\b")))))


#_(test-hiccupize)
#_(test-parse)
#_(test-digitize)


(def some-string (prop/for-all [strings gen/string]
                               (int? (digitize strings))))

(tc/quick-check 1000 some-string)
