(ns Sample.views.welcome
  (:require [Sample.views.common :as common])
  (:use [noir.core]
        [hiccup.page :only [include-css html5]]
        [hiccup.form]))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)
(use 'Sample.database)
(use 'Sample.query)

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to Nimble-Web"]))


(defpartial search-fields [{:keys [searchstr context_ncid]}]
  (label "searchstr" "Search String: ")
  (text-field "searchstr" searchstr)
  (label "context_ncid" "Context NCID: ")
  (text-field "context_ncid" context_ncid))

(defpartial show-result [{:keys [text ncid]}]
  [:tr
   [:td text]
   [:td ncid]])

(defpartial show-results [items]
  [:table
   (map show-result items)])

(defpage "/search" {:as searchterms}
  (common/layout
    (form-to [:post "/search"]
             (search-fields searchterms)
             (submit-button "Search"))))

(defn valid? [{:keys [searchstr context_ncid]}]
  true)

(defn searchncid [{:keys [searchstr context_ncid]}]
  :context_ncid)

(defpage [:post "/search"] {:as searchterms}
  (if (valid? searchterms)
    (common/layout
      [:p (search-concept "asdf" 248)])    
    (render "/search" searchterms)))

