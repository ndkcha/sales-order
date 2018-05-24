(ns sales
  (:gen-class))

(def choice)
(def file)
(def customers)
(def noOfCustomers)
(def customer)
(def cust_details)
(def noOfFields)
(def products)
(def noOfProducts)
(def product)
(def prod_details)

(defn DisplayCustomerTable []
  (def file (slurp "cust.txt"))
  (def customers (sort (clojure.string/split-lines file)))
  (def noOfCustomers (alength (to-array customers)))
  (dotimes [n noOfCustomers]
    (def customer (nth customers n))
    (def cust_details (clojure.string/split customer #"[|]+"))
    (def noOfFields (alength (to-array cust_details)))
    (print (nth cust_details 0) ": [" (nth cust_details 1) "," (nth cust_details 2) "," (nth cust_details 3) "]\n")))

(defn DisplayProductTable []
  (def file (slurp "prod.txt"))
  (def products (sort (clojure.string/split-lines file)))
  (def noOfProducts (alength (to-array products)))
  (dotimes [n noOfProducts]
    (def product (nth products n))
    (def prod_details (clojure.string/split product #"[|]+"))
    (def noOfFields (alength (to-array prod_details)))
    (print (nth prod_details 0) ": [" (nth prod_details 1) "," (nth prod_details 2) "]\n")))

(defn DisplaySalesTable []
  (println "Displaying sales table"))

(defn TotalSalesCustomer []
  (println "Total sales for the customer"))

(defn TotalCountProduct []
  (println "Total count for the product"))

(defn ExitApp []
  (println "Good bye!"))

(defn DisplayMenu []
  (println "1. Display Customer Table")
  (println "2. Display Product Table")
  (println "3. Display Sales Table")
  (println "4. Total Sales for Customer")
  (println "5. Total Count for Product")
  (println "6. Exit")
  (println "Enter your choice:")
  (def choice (read-line))
  (case choice "1" (DisplayCustomerTable)
               "2" (DisplayProductTable)
               "3" (DisplaySalesTable)
               "4" (TotalSalesCustomer)
               "5" (TotalCountProduct)
               "6" (ExitApp)))

(DisplayProductTable)
