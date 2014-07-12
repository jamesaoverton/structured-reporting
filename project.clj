(defproject structured-reporting "0.0.1-SNAPSHOT"
  :description "A prototype structured reporting tool for prostate cancer."
  :url "https://github.com/jamesaoverton/structured-reporting"
  :min-lein-version "2.3.4"

  :source-paths ["src/clj" "src/cljs"]

  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/clojurescript "0.0-2156"]
                 [org.clojure/core.async "0.1.267.0-0d7780-alpha"]
                 [om "0.5.0"]
                 [com.facebook/react "0.9.0"]
                 [org.clojure/data.csv "0.1.2"]]
  
  :plugins [[lein-cljsbuild "1.0.2"]]

  :hooks [leiningen.cljsbuild]

  :main structured-reporting.convert

  :cljsbuild
  {:builds {:structured-reporting
            {:source-paths ["src/cljs"]
             :compiler
             {:output-to "dev-resources/public/js/structured_reporting.js"
              :optimizations :advanced
              :pretty-print false}}}})
