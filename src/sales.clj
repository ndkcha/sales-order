(ns sales
  (:gen-class)
  (:require [clojure.string :as str]))

(def choice)
(def file)
(def customers)
(def customer)
(def cust_details)
(def noOfFields)
(def products)
(def product)
(def prod_details)
(def sales)
(def sale)
(def noOfItems)
(def query)
(def customerName)

(defn toNum [s]
  (if (re-find #"^-?\d+\.?\d*$" s)
    (read-string s)))

(defn SearchFn [i q items]
  (def noOfItems (alength (to-array items)))
  (when (< i noOfItems)
    (if (str/starts-with? (nth items i) q)
      (nth items i)
      (recur (inc i) q items))))

(defn SearchByNameFn [i q items]
  (def noOfItems (alength (to-array items)))
  (def query (str/lower-case q))
  (when (< i noOfItems)
    (if (str/starts-with? (str/lower-case (nth (clojure.string/split (nth items i) #"[|]+") 1)) query)
      (nth items i)
      (recur (inc i) q items))))

(defn FindPriceFn [i productId products]
  (def noOfItems (alength (to-array products)))
  (when (< i noOfItems)
    (if (str/starts-with? (nth products i) productId)
      (toNum (nth (clojure.string/split (nth products i) #"[|]+") 2))
      (recur (inc i) productId products))))


(defn CountSalesFn [i c customerId sales products]
  (if (= i (alength (to-array sales)))
    c
    (when (< i (alength (to-array sales)))
      (def sale (clojure.string/split (nth sales i) #"[|]+"))
      (if (= (nth sale 1) (str customerId))
        (recur (inc i) (+ c (* (FindPriceFn 0 (nth sale 2) products) (toNum (nth sale 3)))) customerId sales products)
        (recur (inc i) c customerId sales products)))))

(defn DisplayCustomerTable []
  (def file (slurp "cust.txt"))
  (def customers (sort (clojure.string/split-lines file)))
  (dotimes [n (alength (to-array customers))]
    (def customer (nth customers n))
    (def cust_details (clojure.string/split customer #"[|]+"))
    (def noOfFields (alength (to-array cust_details)))
    (print (nth cust_details 0) ": [" (nth cust_details 1) "," (nth cust_details 2) "," (nth cust_details 3) "]\n")))

(defn DisplayProductTable []
  (def file (slurp "prod.txt"))
  (def products (sort (clojure.string/split-lines file)))
  (dotimes [n (alength (to-array products))]
    (def product (nth products n))
    (def prod_details (clojure.string/split product #"[|]+"))
    (def noOfFields (alength (to-array prod_details)))
    (print (nth prod_details 0) ": [" (nth prod_details 1) "," (nth prod_details 2) "]\n")))

(defn DisplaySalesTable []
  (def customers (clojure.string/split-lines (slurp "cust.txt")))
  (def products (clojure.string/split-lines (slurp "prod.txt")))
  (def sales (clojure.string/split-lines (slurp "sales.txt")))
  (dotimes [n (alength (to-array sales))]
    (def sale (clojure.string/split (nth sales n) #"[|]+"))
    (def customer (SearchFn 0 (nth sale 1) customers))
    (def product (SearchFn 0 (nth sale 2) products))
    (print (nth sale 0) ": [ ")
    (print (nth (clojure.string/split customer #"[|]+") 1) ", ")
    (print (nth (clojure.string/split product #"[|]+") 1) ", ")
    (print (nth sale 3) "]\n")))

(defn TotalSalesCustomer []
  (def customers (clojure.string/split-lines (slurp "cust.txt")))
  (def products (clojure.string/split-lines (slurp "prod.txt")))
  (def sales (clojure.string/split-lines (slurp "sales.txt")))
  (println "Enter the name: ")
  (def customerName (read-line))
  (def customer (SearchByNameFn 0 customerName customers))
  (println (nth (clojure.string/split customer #"[|]+") 1) ":" (CountSalesFn 0 0.0 (nth customer 0) sales products)))

(defn TotalCountProduct []
  (def products (clojure.string/split-lines (slurp "prod.txt")))
  (def sales (clojure.string/split-lines (slurp "sales.txt")))
  (println "Something"))

(defn ExitApp []
  (println "Good bye!"))

(defn DisplayMenu []
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales for Customer")
  (println "5. Total Count for Product")
  (println "6. Exit")
  (println "Enter your choice: ")
  (def choice (read-line))
  (case choice "1" (DisplayCustomerTable)
               "2" (DisplayProductTable)
               "3" (DisplaySalesTable)
               "4" (TotalSalesCustomer)
               "5" (TotalCountProduct)
               "6" (ExitApp)))

(TotalSalesCustomer)
