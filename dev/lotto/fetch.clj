(ns lotto.fetch
  (:require
   [net.cgrand.enlive-html :as html])
  (:import
   (java.net URI)))

(defn- text->long
  [text]
  {:pre  [(= 1 (count text))]
   :post [(nat-int? %)]}
  ;; Remember that HTML tags can contain a variable number of children that may
  ;; or may not be text. For this reason we need to convert the collection of
  ;; text nodes into a single string.
  (Long/parseUnsignedLong (first text)))

(defn extract-stats
  [doc]
  (let [->result #(text->long (html/select % [:div.result html/text-node]))
        ->count  #(text->long (html/select % [:strong :> :span html/text-node]))
        nodes (html/select doc [:div.tableCell.centred.floatLeft])]
    (into (sorted-map) (map (juxt ->result ->count)) nodes)))

(comment
  (require 'clojure.pprint)
  (def uri (URI. "https://www.national-lottery.com/lotto/statistics"))
  (def doc (html/html-resource uri))

  (spit "resources/lotto.edn"
        (with-out-str (clojure.pprint/pprint (extract-stats doc)))))
