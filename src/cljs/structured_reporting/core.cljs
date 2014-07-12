(ns structured-reporting.core
  (:require [clojure.string :as string]
            [om.core :as om :include-macros true]
            [om.dom :as dom :include-macros true]
            [structured-reporting.data :refer [test-data]]))

; This code is based pretty closely on the Om tutorials:
; https://github.com/swannodette/om/wiki/Basic-Tutorial
; https://github.com/swannodette/om/wiki/Intermediate-Tutorial

(enable-console-print!)

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

(def default-data
  {:patients []
   :patient-search "PS"
   :reports []
   :report-search ""
   :lesions []
   :lesions-search ""})

(def app-state (atom (merge default-data test-data)))

(defn handle-change
  "Synchronize an input element with the application state."
  [e data edit-key owner]
  (om/transact! data edit-key (fn [_] (.. e -target -value))))

(defn editable
  "Create an Om component for a simple text input."
  [data owner {:keys [edit-key] :as opts}]
  (reify
    om/IInitState
    (init-state [_]
      {:editing false})
    om/IRenderState
    (render-state [_ {:keys [editing]}]
      (let [text (get data edit-key)]
        (dom/p nil
          (dom/input
            #js {:value text
                 :onChange #(handle-change % data edit-key owner)}))))))

; We can have a range of specialized `field` functions. This one is basic.

(defn field
  "Return a DIV with the value of a given field (keyword) in the data."
  [data label]
  (dom/div #js {:className (name (to-keyword label))}
           (dom/span #js {:className "label"} (str label ": "))
           (dom/span nil (get data (to-keyword label)))))

;; Patients

(defn patient-view
  "Create an Om component to view a single patient."
  [patient owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id (str "patient-" (:id patient))
                    :className "patient"}
               (field patient "Name")
               (field patient "Date of Birth")
               (field patient "History")))))

(defn patients-view
  "Create an Om component to view a list of patients."
  [app owner]
  (reify
    om/IRenderState
    (render-state [_ state]
      (dom/div nil
               (dom/h2 nil "Patients")
               (om/build editable app
                         {:opts {:edit-key :patient-search}})
               (apply dom/div #js {:id "patient-list"}
                      (om/build-all patient-view (:patients app)))))))

(om/root
  patients-view
  app-state
  {:target (. js/document (getElementById "patients"))})


;; Reports

; We should probably distiguish between compact and full views of reports.

(defn report-view
  "Create an Om component to view a single report."
  [report owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id (str "report-" (:id report))
                    :className "report"}
               (field report "Patient")
               (field report "Date of Exam")
               (field report "Date of Report")
               (field report "Modality")
               (field report "Prostate Size Transverse")
               (field report "Prostate Size Anterior-Posterior")
               (field report "Prostate Size Longitudinal")
               (field report "Measured PSA")
               (field report "BPH Change")
               (field report "Peripheral Zone Diffuse Anomaly")
               (field report "Transition Zone Diffuse Anomaly")
               (field report "Seminal Vesicles")
               (field report "Technique")
               (field report "Conclusions")))))

(defn reports-view
  "Create an Om component to view a list of reports."
  [app owner]
  (reify
    om/IRenderState
    (render-state [_ state]
      (dom/div nil
               (dom/h2 nil "Reports")
               (om/build editable app
                         {:opts {:edit-key :report-search}})
               (apply dom/div #js {:id "report-list"}
                      (om/build-all report-view (:reports app)))))))

(om/root
  reports-view
  app-state
  {:target (. js/document (getElementById "reports"))})


;; Lesions

(defn observation-view
  "Create an Om component to view a single observation."
  [observation owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id (str "observation-" (:id observation))
                    :className "observation"}
               (field observation "Type")
               (field observation "Observed")
               (field observation "Hyperintense")
               (field observation "Isointense")
               (field observation "Hypointense")
               (field observation "Rim")))))

; We should probably distiguish between compact and full views of lesions.

(defn lesion-view
  "Create an Om component to view a single lesion."
  [lesion owner]
  (reify
    om/IRender
    (render [_]
      (dom/div #js {:id (str "lesion-" (:id lesion))
                    :className "lesion"}
               (field lesion "Report")
               (field lesion "Location Transverse")
               (field lesion "Location Anterior-Posterior")
               (field lesion "Location Longitudinal")
               (field lesion "Size Transverse")
               (field lesion "Size Anterior-Posterior")
               (field lesion "Size Longitudinal")
               (field lesion "Zone")
               (field lesion "Capsule")
               (apply dom/div
                      #js {:className "observation-list"}
                      (om/build-all observation-view
                                    (:observations lesion)))))))

(defn merge-lesions
  "Merge all observations into the :observations vector of the parent
   lesion maps and return a vector of lesion maps."
  [app]
  (->> (:mri-observations app)
       (reduce
         (fn [lesions observation]
           (update-in lesions
                      [(:lesion observation) :observations]
                      (fnil conj [])
                      observation))
         (zipmap (map :id (:lesions app)) (:lesions app)))
       vals
       vec))

(defn lesions-view
  "Create an Om component to view a list of lesions."
  [app owner]
  (reify
    om/IRenderState
    (render-state [_ state]
      (dom/div nil
               (dom/h2 nil "Lesions")
               (om/build editable app
                         {:opts {:edit-key :lesion-search}})
               (apply dom/div #js {:id "lesion-list"}
                      (om/build-all lesion-view
                                    (merge-lesions app)))))))

(om/root
  lesions-view
  app-state
  {:target (. js/document (getElementById "lesions"))})


;; TODO: Analysis

; Graphs of selected data.


;; TODO: Diagram

; Visualization of selected report.


;; TODO: Text-Report

; Textual representation of selected report.


