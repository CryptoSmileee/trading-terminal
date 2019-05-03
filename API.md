
<p align="center">
  <a href="https://orionprotocol.io">
    <img src="https://res.cloudinary.com/dnbcgedbu/image/upload/v1556261195/photo_2019-04-26_08-42-57.jpg" />
  </a>
</p>

<h3 align="center">Orion Broker API Documentation</h3>

# API

- [API](#api)
- [Broker Api](#broker-api)
	- [Register](#register)
		- [HTTP Request](#http-request)
		- [Body Parameters](#body-parameters)
	- [Balance](#balance)
		- [HTTP Request](#http-request-1)
		- [Body Parameters](#body-parameters-1)
- [Exchange Web Api](#exchange-web-api)
	- [Order book](#order-book)
		- [HTTP Request](#http-request-2)
		- [Query Parameters](#query-parameters)
	- [Order](#order)
		- [HTTP Request](#http-request-3)
		- [Query Parameters](#query-parameters-1)
- [Order Api](#order-api)
	- [Order](#order-1)
		- [HTTP Request](#http-request-4)
		- [Query Parameters](#query-parameters-2)
	- [Order](#order-2)
		- [HTTP Request](#http-request-5)
		- [Body Parameters](#body-parameters-2)
	- [Order](#order-3)
		- [HTTP Request](#http-request-6)
		- [Body Parameters](#body-parameters-3)
	- [History](#history)
		- [HTTP Request](#http-request-7)
		- [Query Parameters](#query-parameters-3)
- [Order Benefits Api](#order-benefits-api)
	- [Order benefits](#order-benefits)
		- [HTTP Request](#http-request-8)
		- [Query Parameters](#query-parameters-4)
- [Pairs Api](#pairs-api)
	- [List](#list)
		- [HTTP Request](#http-request-9)
- [Trade Api](#trade-api)
	- [Trade](#trade)
		- [HTTP Request](#http-request-10)
		- [BODY Parameters](#body-parameters)

# Broker Api

## Register

This endpoint register a broker

### HTTP Request

`POST http://example.com/broker/register`

### Body Parameters

Parameter | Default | Description | Value
-|-|-|-
address || Broker address | String
publicKey || Broker public key | String
callbackUrl ||| String
signature ||| String

## Balance

This endpoint retrieves the balance for a broker

### HTTP Request

`POST http://example.com/broker/balance`

### Body Parameters

Parameter | Default | Description | Value
-|-|-|-
address || Broker address | String

# Exchange Web Api

## Order book

This endpoint retrieves the order books from all supported exchanges

### HTTP Request

`GET http://example.com/api/v1/orderBook`

### Query Parameters

Parameter | Default | Description | Value
-|-|-|-
symbol || Trading pair | String, e.g. - BTC_ETH
depth ||| Integer

## Order

This endpoint retrieves the order book for a single exchange

### HTTP Request

`GET http://example.com/api/v1/exchange/orderBook`

### Query Parameters

Parameter | Default | Description | Value
-|-|-|-
symbol || Trading pair | String, e.g. - BTC_ETH
depth ||| Integer
exchange || Exchange name | String, e.g. - Binance

# Order Api

## Order

This endpoint retrieves information for a single order

### HTTP Request

`GET http://example.com/order`

### Query Parameters

Parameter | Default | Description | Value
-|-|-|-
orderId || Order id | Long Integer

## Order

This endpoint create a single order

### HTTP Request

`POST http://example.com/order`

### Body Parameters

Parameter | Default | Description | Value
-|-|-|-
clientId || Client id | String
symbol || Trading pair | String, e.g. - BTC_ETH
clientOrderId || Client order id | String
side || Order side | String, e.g. - BUY / SELL
orderQty || Order quantity | Double
price || Price | Double
ordType ||| String

## Order

This endpoint delete a single order

### HTTP Request

`DELETE http://example.com/order`

### Body Parameters

Parameter | Default | Description | Value
-|-|-|-
orderId || Order id | Long Integer
clientOrderId || Client order id | String

## History

This endpoint retrieves the history

### HTTP Request

`GET http://example.com/orderHistory`

### Query Parameters

Parameter | Default | Description | Value
-|-|-|-
ordId || Order id | Long Integer
symbol || Trading pair | String, e.g. - BTC_ETH
startTime || Start time | Long Integer
endTime || End time | Long Integer
address ||| String
limit | 500 | Limit order amount | Integer

# Order Benefits Api

## Order benefits

//

### HTTP Request

`GET http://example.com/order-benefits`

### Query Parameters

Parameter | Default | Description | Value
-|-|-|-
symbol || Trading pair | String, e.g. - BTC_ETH
ordQty  || Order quantity | Double
side || Order side | String, e.g. - BUY / SELL

# Pairs Api

## List

This endpoint retrieves the pairs list

### HTTP Request

`GET http://example.com/pairs/list`

# Trade Api

## Trade

//

### HTTP Request

`POST http://example.com/trade`

### BODY Parameters

Parameter | Default | Description | Value
-|-|-|-
orderId || Order id | Long Integer