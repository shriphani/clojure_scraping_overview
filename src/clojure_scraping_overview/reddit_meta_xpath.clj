(ns clojure-scraping-overview.reddit-meta-xpath
  "XPath based scraping of the html"
  (:require [clj-http.client :as client]
            [clojure-scraping-overview.core :as core])
  (:use [clj-xpath.core :only [$x:node* $x:text*]])
  (:import [org.htmlcleaner HtmlCleaner DomSerializer CleanerProperties]))

(defn get-link
  [a-link]
  (let [page (-> a-link client/get :body)]
    (core/html->xml page)))

(def *title-xpath* "//html/body/div[contains(@class, 'content')]/div[contains(@class, 'spacer')]/div[contains(@id, 'siteTable')]/div[contains(@class, 'thing')]/div[contains(@class, 'entry')]/p[contains(@class, 'title')]/a[contains(@class, 'title')]")

(def *points-xpath* "//html/body/div[contains(@class, 'content')]/div[contains(@class, 'spacer')]/div[contains(@id, 'siteTable')]/div[contains(@class, 'thing')]/div[contains(@class, 'midcol')]/div[contains(@class, 'score')]")

(def *username-xpath* "//html/body/div[contains(@class, 'content')]/div[contains(@class, 'spacer')]/div[contains(@id, 'siteTable')]/div[contains(@class, 'thing')]/div[contains(@class, 'entry')]/p[contains(@class, 'tagline')]/a[contains(@class, 'author')]")

(def *comments-xpath* "//html/body/div[contains(@class, 'content')]/div[contains(@class, 'spacer')]/div[contains(@id, 'siteTable')]/div[contains(@class, 'thing')]/div[contains(@class, 'entry')]/ul[contains(@class, 'flat-list')]/li[contains(@class, 'first')]/a[contains(@class, 'comments')]")

(defn extract-info
  []
  (let [document (get-link "http://reddit.com/")

        titles     ($x:text* *title-xpath* document)
        points     ($x:text* *points-xpath* document)
        submitters ($x:text* *username-xpath* document)
        n-comments ($x:text* *comments-xpath* document)]
    (map
     (fn [[t p u c]]
       {:title t
        :submitter u
        :comments c
        :points p})
     (map vector titles points submitters n-comments))))
