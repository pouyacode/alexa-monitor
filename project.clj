(defproject alexa-monitor "0.1.0-SNAPSHOT"
  :description "Simple web crawler to guide new programmers in Clojure world."
  :url "https://alexa-monitor.pouyacode.net"
  :license {:name "EPL-2.0 OR GPL-2.0-or-later WITH Classpath-exception-2.0"
            :url "https://www.eclipse.org/legal/epl-2.0/"}
  :dependencies [[org.clojure/clojure "1.11.0-alpha1"]
                 [hato "0.8.1"]
                 [hickory "0.7.1"]
                 [org.clojure/java.jdbc "0.7.12"]
                 [org.xerial/sqlite-jdbc "3.34.0"]
                 [overtone/at-at "1.2.0"]
                 [org.clojure/test.check "1.1.0"]]
  :main ^:skip-aot alexa-monitor.core
  :target-path "target/%s"
  :profiles {:dev {:plugins [[lein-shell "0.5.0"]]}
             :uberjar {:aot :all}
             :ring {:handler example/app}}
  :plugins [[lein-marginalia "0.9.1"]]
  :aliases
  {"native"
   ["shell"
    "native-image" "--report-unsupported-elements-at-runtime"
    "--initialize-at-build-time"
    "--enable-url-protocols=https"
    "--enable-url-protocols=http"
    "-jar" "./target/uberjar/${:uberjar-name:-${:name}-${:version}-standalone.jar}"
    "-H:Name=./target/${:name}"]})
