(ns com.time-tracker.repl
  (:require [com.biffweb :as biff :refer [q]]))

(defn get-sys []
  (biff/assoc-db @biff/system))

(comment

  ;; If I eval (biff/refresh) with Conjure, it starts sending stdout to Vim.
  ;; fix-print makes sure stdout keeps going to the terminal.
  (biff/fix-print (biff/refresh))

  (let [{:keys [biff/db] :as sys} (get-sys)]
    (q db
       '{:find (pull user [*])
         :where [[user :user/email]]})
    )

  (let [{:keys [biff/db] :as sys} (get-sys)]
    (q db
       '{:find (pull tag [*])
         :where [[tag :tag/name]]})
    )



  (let [{:keys [biff/db] :as sys} (get-sys)]
    (biff/submit-tx sys
     [{:db/op :create
       :db/doc-type :tag
       ;; :xt/id (:uid (java.util.UUID/randomUUID))
       ;; :tag/published :db/now
       :tag/name "test002"
       :tag/description "testbeschreibung002"
       :tag/color "123"}])
    )


  (sort (keys @biff/system))

  )
;; => nil
