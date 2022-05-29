(ns com.time-tracker.utils
  (:require [com.biffweb :as biff :refer [q]]
            [com.time-tracker.ui :as ui]
            [rum.core :as rum]
            [xtdb.api :as xt]
            [ring.adapter.jetty9 :as jetty]
            [cheshire.core :as cheshire]))

(defn parse-uuid [s]
  (biff/catchall (java.util.UUID/fromString s)))
