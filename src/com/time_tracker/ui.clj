(ns com.time-tracker.ui
  (:require [clojure.java.io :as io]
            [com.biffweb :as biff]))

(def nav [{:name "Home" :route "/app"}
          {:name "Projects" :route "/app/projects"}
          {:name "Tags" :route "/app/tags"}])

(defn navbar [nav]
  [:nav.flex.items-center.justify-between.flex-wrap.bg-gray-800.p-6
   [:div.flex.items-center.flex-shrink-0.text-white.mr-6
    [:span.font-bold.text-xl "Trakkr"]]
   [:div.w-full.block.flex-grow.sm:flex.sm:items-center.sm:w-auto
    [:div.text-sm.sm:flex-grow
     (for [item nav]
       [:a.block.mt-4.sm:inline-block.sm:mt-0.text-teal-200.hover:text-white.mr-4
        {:href (:route item)} (:name item)])]]])

(defn css-path []
  (if-some [f (io/file (io/resource "public/css/main.css"))]
    (str "/css/main.css?t=" (.lastModified f))
    "/css/main.css"))

(defn base [opts & body]
  (apply
    biff/base-html
    (-> opts
        (merge #:base{:title "My Application"
                      :lang "en-US"
                      :icon "/img/glider.png"
                      :description "My Application Description"
                      :image "https://clojure.org/images/clojure-logo-120b.png"})
        (update :base/head (fn [head]
                             (concat [[:link {:rel "stylesheet" :href (css-path)}]
                                      [:script {:src "https://unpkg.com/htmx.org@1.6.1"}]
                                      [:script {:src "https://unpkg.com/hyperscript.org@0.9.3"}]]
                                     head))))
    body))

(defn page [opts & body]
  (base
    opts
    [:.p-3.mx-auto.max-w-screen-xl.w-full
     (navbar nav)
     body]))
