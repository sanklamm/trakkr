(ns com.time-tracker.feat.tags
  (:require
   [com.biffweb :as biff :refer [q]]
   [rum.core :as rum]
   [xtdb.api :as xt]
   [com.time-tracker.ui :as ui]
   [com.time-tracker.utils :as utils]))

(defn tag-list
  [{:keys [session biff/db] :as req}]
  (let [tags (q db
                '{:find (pull tag [*])
                  :where [[tag :tag/name]]})]
    (ui/page
     {}
     [:div
      [:h1 "Tags"]
      [:.h-3]
      [:a.btn {:href "/app/tags-new"} "Tag erstellen"]
      (for [tag tags]
        [:ul
         [:li 
          [:div.rounded-lg {:style {:background-color (:tag/color tag)
                                    :padding "10px"}}
           [:a.link {:href (str "/app/tags/" (:xt/id tag))} (str (:tag/name tag) " - " (:tag/description tag) " - ")]]]])])))

(defn new-tag-page
  [{:keys [path-params biff/db] :as req}]
  (ui/page
   {}
   [:h1 "Tag erstellen"]
   (biff/form
    {:hx-post "/app/tags"
     :hx-swap "outerHTML"}
    [:.flex
     [:label.block {:for "name"} "Name: "]
     [:.w-3]
     [:input.w-full#name {:type "text" :name "name"}]]
    [:.h-1]
    [:.flex
     [:label.block {:for "description"} "Beschreibung: "]
     [:.w-3]
     [:input.w-full#description {:type "text" :name "description"}]]
    [:.h-1]
    [:.flex
     [:label.block {:for "color"} "Farbe (CSS-Code): "]
     [:.w-3]
     [:input.w-full#color {:type "text" :name "color"}]]
    [:.h-1]
    [:button.btn {:type "submit"} "Speichern"]
    ,)))

(defn edit-tag-page
  [{:keys [path-params biff/db] :as req}]
  (let [tag-id (java.util.UUID/fromString (:id path-params))
        tag (xt/entity db tag-id)]
    (ui/page
     {}
     [:h1 "Tag editieren"]
     [:div
      [:pre (pr-str tag)]]
     (biff/form
      {:action (str "/app/tags/" tag-id "/delete")
       :class "inline"}
      [:button.text-blue-500.hover:text-blue-800 {:type "submit"}
       "Delete"])
     (biff/form
      {:hx-post (str "/app/tags/" tag-id)
       :hx-swap "outerHTML"}
      [:input {:type "hidden" :name "id" :value tag-id}]
      [:.w-3]
      [:.flex
       [:br]
       [:label.block {:for "name"} "Name: "]
       [:.w-3]
       [:input.w-full#name {:type "text" :name "name" :value (:tag/name tag)}]]
      [:.h-1]
      [:.flex
       [:label.block {:for "description"} "Beschreibung: "]
       [:.w-3]
       [:input.w-full#description {:type "text" :name "description" :value (:tag/description tag)}]]
      [:.h-1]
      [:.flex
       [:label.block {:for "color"} "Farbe (CSS-Code): "]
       [:.w-3]
       [:input.w-full#color {:type "text" :name "color" :value (:tag/color tag)}]]
      [:.h-1]
      [:button.btn {:type "submit"} "Speichern"]
      ,))))


(defn tag-update [{:keys [session params] :as req}]
  (biff/pprint params)
  (biff/submit-tx req
                  [{:db/op :update
                    :db/doc-type :tag
                    :xt/id (utils/parse-uuid (:id params))
                    :tag/name (:name params)
                    :tag/description (:description params)
                    :tag/color (:color params)}])
  #_(biff/render (tag-form {:name (:name params)
                            :description (:description params)
                            :color (:color params)})))

(defn tag-create [{:keys [session params] :as req}]
  (prn params)
  (biff/submit-tx req
                  [{:db/op :create
                    :db/doc-type :tag
                    ;; :xt/id (:uid session)
                    :tag/name (:name params)
                    :tag/description (:description params)
                    :tag/color (:color params)}])
  #_(biff/render (tag-form {:name (:name params)
                            :description (:description params)
                            :color (:color params)})))

(defn tag-delete [{:keys [path-params] :as req}]
  (prn "Delete Tag")
  (biff/pprint path-params)
  (biff/submit-tx req
                  [{:db/op :delete
                    :xt/id (utils/parse-uuid (:id path-params))}])
  {:status 303
   :headers {"location" "/app/tags"}})
