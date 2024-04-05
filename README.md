# Coffee Shop managment Backend 

  ## Requirments
    - Java 17+
    - MYSQL -> create a schema named coffee here ( and get the credentials here src/main/resources/application.properties to match your env)
  ## To install the dependancies go to the repo root folder and run
  ```
    > .\mvnw install
  ```
  ## To run the app run this 
  ```
     .\mvnw spring-boot:run
  ```
  that's it the app is running now ( atyou can browser it @ http://localhost:8080/auth/welcome )

# Using the API so now let's use API to Add some data

##  Add users (/auth/addNewUser)
```
curl --location --request POST 'http://localhost:8080/auth/addNewUser' \
--header 'Content-Type: application/json' \
--data-raw '{
"password" : "pass",
"name":"admin",
"email" : "tin.spu@gmail.com",
"roles" : "USER_ROLE"
}'
```
##  generate a token 
```
curl --location --request POST 'http://localhost:8080/auth/generateToken' \
--header 'Content-Type: application/json' \
--data-raw '{
"password" : "pass",
"username" : "admin"
}'
```
 use the JWT token returned from the above call to communicate with the API to add products or create orders ( the orders are filtered by the currect user )
e.g.
## OPENAPI doc here 
    - (http://localhost:8080/swagger-ui/index.html#/)

##  create a product 
```
curl --location --request POST 'http://localhost:8080/products' \
--header 'Authorization: Bearer [THE_JWT_TOKEN_FROM_THE_ABOVE_CALL]' \
--header 'Content-Type: application/json' \
--data-raw '{
 "name" : "ProductX",
 "price" : 10.35
}'

returns a json with a details and a productid

```
##  create an order ( for the above product , we use the id ref from the above call response to determine the product id
```
curl --location --request POST 'http://localhost:8080/orders' \
--header 'Authorization: Bearer [THE_JWT_TOKEN_FROM_THE_ABOVE_CALL]' \
--header 'Content-Type: application/json' \
--data-raw '{
    "orderItems": [   ---> these are the list of items in each order ( one or more items on each order)
      {
        "product" : { 
            "id": THE_PRODUCT_ID_FROM_THE_ABOVE_CALL OR FROM LIST PRODUCTS PAGE( on the client side we can have a cart thigie)
            },                      ^  
                                    |
        "quantity": 2 --- > quanitty for this product id
      },      {
        "product" : { 
            "id": ANOTHER_PRODUCT_ID_FROM_THE_ABOVE_CALL OR FROM LIST PRODUCTS PAGE( on the client side we can have a cart thigie)
            },
        "quantity": 5 ---> quanitty for this product id
      }
    ]
  }'
 ```

 

That's it for now.

 
