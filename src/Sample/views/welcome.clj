(ns Sample.views.welcome
  (:require [Sample.views.common :as common])
  (:use [noir.core :only [defpage]]))

(defpage "/welcome" []
         (common/layout
           [:p "Welcome to Nimble-Web"]))

(defpage "/search" []
  (common/layout
    [:h1 "this is the search page!"]))


