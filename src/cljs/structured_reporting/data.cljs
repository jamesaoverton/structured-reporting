(ns structured-reporting.data)

(def test-data {:patients [{:history "History for Patient 1.", :date-of-birth "1967-03-04", :name "Test Patient 1", :id "1"}], :reports [{:peripheral-zone-diffuse-anomaly "hyperintense", :prostate-size-transverse "40", :date-of-report "2012-07-01", :date-of-exam "2012-07-01", :modality "MRI", :measured-psa "6", :conclusions "This is a conclusion.", :seminal-vesicles "normal", :bph-change "moderate", :transition-zone-diffuse-anomaly "none", :technique "The exam was performed at 3T, with an endorectal coil. The following sequences were performed: Saggital, axial, and coronal T2 weighted; DWI with b values 100 and 800; ADC map; T1 weighted dynamic contrast enhancement.", :patient "1", :prostate-size-anterior-posterior "42", :prostate-size-longitudinal "59", :id "1"}], :lesions [{:location-transverse "30", :zone "both", :size-anterior-posterior "4", :capsule "inside", :report "1", :location-anterior-posterior "40", :size-transverse "3", :location-longitudinal "50", :size-longitudinal "5", :id "1"}], :mri-observations [{:rim "continuous", :hypointense "FALSE", :isointense "TRUE", :hyperintense "FALSE", :observed "TRUE", :type "T1", :lesion "1", :id "1"} {:rim "discontinous", :hypointense "FALSE", :isointense "FALSE", :hyperintense "FALSE", :observed "TRUE", :type "T2", :lesion "1", :id "2"} {:rim "none", :hypointense "FALSE", :isointense "TRUE", :hyperintense "TRUE", :observed "TRUE", :type "diffusion", :lesion "1", :id "3"} {:rim "none", :hypointense "FALSE", :isointense "TRUE", :hyperintense "FALSE", :observed "TRUE", :type "ADC", :lesion "1", :id "4"} {:rim "none", :hypointense "TRUE", :isointense "FALSE", :hyperintense "FALSE", :observed "TRUE", :type "early", :lesion "1", :id "5"} {:rim "none", :hypointense "FALSE", :isointense "FALSE", :hyperintense "FALSE", :observed "FALSE", :type "late", :lesion "1", :id "6"}]})