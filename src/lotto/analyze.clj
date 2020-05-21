(ns lotto.analyze
  (:require
   [clojure.edn :as edn]
   [clojure.java.io :as io])
  (:import
   (java.io PushbackReader)))

(defn read-stats
  [resource]
  (with-open [rdr (io/reader resource)]
    (edn/read (PushbackReader. rdr))))

(defn most-frequent
  ([stats]   (most-frequent stats 5))
  ([stats n] (into {} (take n (sort-by val > stats)))))

(comment
  (most-frequent (read-stats (io/resource "lotto.edn"))))
