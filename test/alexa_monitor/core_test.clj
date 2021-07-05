(ns alexa-monitor.core-test
  (:require [clojure.test :refer :all]
            [alexa-monitor.core :refer :all]))


(deftest test-universe
  (testing "Let's see if the universe is as sane as we want."
    (is (= true (not false)))))
