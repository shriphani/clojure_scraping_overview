(ns clojure-scraping-overview.reddit-meta-enlive
  (:require [clj-http.client :as client]
            [clojure-scraping-overview.core :as core]
            [net.cgrand.enlive-html :as html]))

(defn get-link
  [a-link]
  (-> a-link client/get :body))

(def *title-selector*  [:div#siteTable :p.title])
(def *points-selector* [:div#siteTable :div.midcol :div.score])
(def *username-selector* [:div#siteTable :div.entry :p.tagline :a.author])
(def *comments-selector* [:div#siteTable :div.entry :ul.flat-list :li.first :a.comments])

(defn select-item
  [doc sel]
  (map
   html/text
   (html/select
    (html/html-resource
     (java.io.StringReader. doc))
    sel)))

(defn extract-info
  []
  (let [document (get-link "http://reddit.com")
        
        titles   (select-item document *title-selector*)
        points   (select-item document *points-selector*)
        uname    (select-item document *username-selector*)
        comments (select-item document *comments-selector*)]
    (map
     (fn [[t p u c]]
       {:title t
        :submitter u
        :comments c
        :points p})
     (map vector titles points uname comments))))

(defn extract-info2
  []
  (let [document (get-link "http://reddit.com")]
    (map
     (fn [[t p u c]]
       {:title t
        :submitter u
        :comments c
        :points p})
     (partition
      4
      4
      (select-item
       document
       #{*title-selector*
         *points-selector*
         *username-selector*
         *comments-selector*})))))
