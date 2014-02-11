(ns clojure-scraping-overview.core
  (:import [org.htmlcleaner HtmlCleaner DomSerializer CleanerProperties]))

(defn html->xml
  [a-html-doc]
  (let [cleaner (new HtmlCleaner)
        
        cleaner-props (new CleanerProperties)
        dom-srlzr     (new DomSerializer cleaner-props)
        
        cleaned-doc   (.clean cleaner a-html-doc)]
    
    (.createDOM dom-srlzr cleaned-doc)))

