(ns com.time-tracker.schema
  (:require [malli.core :as malc]
            [malli.registry :as malr]))

(def schema
  {:user/id :uuid
   :user/email :string
   :user/foo :string
   :user/bar :string
   :user [:map {:closed true}
          [:xt/id :user/id]
          :user/email
          [:user/foo {:optional true}]
          [:user/bar {:optional true}]]

   :report/id :uuid
   :report/owner :user/id
   :report/date inst?
   :report/notes :string
   :report [:map {:closed true}
            [:xt/id :report/id]
            :report/owner
            :report/date
            [:report/notes {:optional true}]]

   :tag/id :uuid
   :tag/owner :user/id
   :tag/name :string
   :tag/description :string
   :tag/color :string
   :tag [:map {:closed true}
         [:xt/id :tag/id]
         [:tag/owner {:optional true}]
         :tag/name
         :tag/color
         [:tag/description {:optional true}]]

   :time-entry/id :uuid
   :time-entry/owner :user/id
   :time-entry/project :project/id
   :time-entry/tag :tag/id
   :time-entry/start inst?
   :time-entry/end inst?
   :time-entry/description :string
   :time-entry/notes :string
   :time-entry [:map {:closed true}
                [:xt/id :time-entry/id]
                :time-entry/owner
                [:time-entry/project {:optional true}]
                [:time-entry/tag {:optional true}]
                [:time-entry/start {:optional true}]
                [:time-entry/end {:optional true}]
                [:time-entry/description {:optional true}]
                [:time-entry/notes {:optional true}]]

   :project/id :uuid
   :project/owner :user/id
   :project/name :string
   :project/client :string
   :project/live-url :string
   :project/staging-url :string
   :project/path :string
   :project/gitlab-url :string
   :project/ident-internal :string
   :project/ident-external :string
   :project [:map {:closed true}
             [:xt/id :time-entry/id]
             :project/owner
             :project/name
             [:project/client {:optional true}]
             [:project/live-url {:optional true}]
             [:project/staging-url {:optional true}]
             [:project/path {:optional true}]
             [:project/gitlab-url {:optional true}]
             [:project/ident-internal {:optional true}]
             [:project/ident-external {:optional true}]]

   :msg/id :uuid
   :msg/user :user/id
   :msg/text :string
   :msg/sent-at inst?
   :msg [:map {:closed true}
         [:xt/id :msg/id]
         :msg/user
         :msg/text
         :msg/sent-at]})

(def malli-opts {:registry (malr/composite-registry malc/default-registry schema)})
