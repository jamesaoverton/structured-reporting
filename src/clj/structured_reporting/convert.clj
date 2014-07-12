(ns structured-reporting.convert
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [clojure.data.csv :as csv]))

(defn to-keyword
  "Take a string and return a properly formatted keyword."
  [s]
  (-> s
      string/trim
      string/lower-case
      (string/replace "_" "-")
      (string/replace "/" "-")
      (string/replace #"\s+" "-")
      keyword))

(defn read-rows
  "Given sequence of sequences where the first sequence contains headers,
  return a sequence of maps with the headers as keys."
  [rows]
  (mapv (partial zipmap (map to-keyword (first rows)))
        (rest rows)))

(defn read-tsv
  "Load data from the tab-separated values file at the given path,
   returning a sequence of maps.
   Handle non-Unix line-endings (inefficiently)."
  [path]
  (read-rows
    (csv/read-csv (string/replace (slurp path) #"\r\n|\r" "\n")
                  :separator \tab)))

(defn insert-data
  "Given an id-field (keyword),
   a parent-field (keyword), a sequence of maps of the parents,
   a child-field (keyword), and a sequence of maps of the children,
   for each parent map add the child-field key with value a vector of children,
   and return the new nested data structure.
   Use vectors instead of lists throughout to make Om happy.
   Example:
     (insert-data :id
                  :parent   [{:id 1 :name \"parent\"}]
                  :children [{:parent 1 :name \"child\"}])
     => [{:id 1 :name \"parent\" :children [{:parent 1 :name \"child\"}]]"
  [id-field parent-field parents child-field children]
  (->> children
       (reduce
         (fn [parents child]
           (update-in parents
                      [(get child parent-field) child-field]
                      (fnil conj [])
                      child))
         (zipmap (map id-field parents) parents))
       vals
       vec))

#_(defn load-data
  "Load sequences of maps from several TSV files,
   then insert them into a nested data structure."
  []
  (->> (read-tsv "data/mri_observations.tsv")
       (insert-data :id :lesion  (read-tsv "data/lesions.tsv")  :observations)
       (insert-data :id :report  (read-tsv "data/reports.tsv")  :lesions)
       (insert-data :id :patient (read-tsv "data/patients.tsv") :reports)
       vec))

(defn load-data
  "Load several tables into a single nested data structure."
  []
  {:patients (read-tsv "data/patients.tsv")
   :reports  (read-tsv "data/reports.tsv")
   :lesions  (read-tsv "data/lesions.tsv")
   :mri-observations (read-tsv "data/mri_observations.tsv")})

(defn write-data
  "Load data from TSV files and write it in EDN format to the data.cljs file."
  []
  (with-open [w (io/writer "src/cljs/structured_reporting/data.cljs")]
    (.write w "(ns structured-reporting.data)\n\n")
    (.write w "(def test-data ")
    (try
      (.write w (pr-str (load-data)))
      (catch Exception e (.write w "nil")))
    (.write w ")\n")))

(defn -main
  "Main entry point: run `write-data` and exit."
  [& args]
  (write-data))

