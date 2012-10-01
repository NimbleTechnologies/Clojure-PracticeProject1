(ns Sample.views.welcome
  (:require [Sample.views.common :as common])
  (:use [noir.core]
        [hiccup.page :only [include-css html5]]
        [hiccup.form]))

(use '[datomic.api :only [q db] :as d])
(use 'clojure.pprint)

(def uri "datomic:free://localhost:4334//hdd")

(def conn (d/connect uri))

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

(defpage [:post "/search"] {:as searchterms}
  (if (valid? searchterms)
    (common/layout
      [:p "On Search!"])    
    (render "/search" searchterms)))
