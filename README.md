# structured-reporting

This is an early prototype of a structured reporting tool for radiology and pathology of prostate cancer. It runs in modern web browsers such as Chrome. The main technoloies used are [Clojure](http://clojure.org), [ClojureScript](https://github.com/clojure/clojurescript), and [Om](https://github.com/swannodette/om).


## Usage

Make sure you have [Leiningen](http://leiningen.org) installed, then:

1. Check out this repository:

        git clone https://github.com/jamesaoverton/structured-reporting.git
        cd structure-reporting

2. Convert the tables in `data` to the file `src/cljs/structured_reporting/data.cljs` (using the code in `src/clj/structured_reporting/convert.clj`):

        lein run

3. Compile all the ClojureScript code in `src/cljs` to a single JavaScript file (`dev-sources/public/js/structured_reporting.js`):

        lein cljsbuild once

4. Open the `dev-resources/public/index.html` file in your browser.


## Data

Our current data model has four tables and is limited to MRI imaging. A patient can have zero or more reports; a report can have zero or more lesions; a lesion will have a fixed number of observations, depending on the modality of the imaging.

- patients
  - ID: unique identifier for this patient
  - Name: patient's full name or identifier
  - Date of Birth -- YYYY-MM-DD
  - History: a summary of the patient's medical history -- free text
- reports
  - ID: unique identfier for this report
  - Patient: the ID of the patient
  - Exam Date -- YYYY-MM-DD
  - Report Date: some time after the exam date -- YYYY-MM-DD
  - Modality: currently only MRI
  - Prostate Size [Axis]: in millimeters along this axis
  - Measured PSA: the measured Prostate Specific Antigen level previous to this report
  - BPH Change: Benign Prostatic Hyperplasia -- mild, mild to moderate, moderate, moderate to severe, severe (NOTE: may be changed to an numeric value)
  - Peripheral Zone Diffuse Anomaly -- none, hyperintense, hypointense, heterogeneous
  - Transition Zone Diffuse Anomaly -- none, hyperintense, hypointense, heterogeneous
  - Seminal Vesicles: normal, hypertrophic, hypotrophic
  - Technique: how the exam was performed -- free text
  - Concluions -- free text
- lesions: a region of interest within the prostate
  - ID: unique identifier for this lesion (in this report -- we don't yet link lesions between reports)
  - Report: the ID of the report for this lesion
  - Location [Axis]: in millimeters from the centre of the prostate along this axis
  - Size [Axis]: in millimetres along this axis
  - Zone: qualitiative location in the prostate -- peripheral zone, transition zone, both zones
  - Capsule: location relative to the capsule of the prostate -- inside, abutting, beyond
- mri-observations: characteristics of a lesion in a given MRI sequence
  - ID: unique identifier for this mri-observation
  - Lesion: the ID of the lesion that this mri-observation entry is about
  - Type: the MRI sequence type -- T1, T2, diffusion, ADC, early phase contrast, late phase contrast
  - Observed: is the lesion observed in this sequence? -- true or false
  - Hyperintense: is the lesion more intense than the surrounding tissue? -- true or false
  - Isointense: is the lesion as intense as the surrounding tissue? -- true or false
  - Hypointense: is the lesion less intense than the surrounding tissue? -- true or false
  - Rim: is there a rim around the lesion? -- none, continous, discontinuous


## Development

This tool is based on the [om-start-template](https://github.com/magomimmo/om-start-template). See those instructions for enabling interactive development with a browser-based REPL.

TODO: unit testing, integration testing


## License

Copyright © 2014 James A. Overton

