(ns Sample.views.common
  (:use [noir.core]
        [hiccup.page :only [include-css html5]]
        [hiccup.form]))

(require '[noir.validation :as vali])
(require '[noir.response :as resp])

(defpartial layout [& content]
            (html5
              [:head
               [:title "Nimble Web"]
               (include-css "/css/default.css")]
              [:body
               [:div#wrapper
                content]]))
