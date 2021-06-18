# Books api
Quick book storage, update, access and total price calculation
## Usage

Get books grouped by quantity
```
GET http://{hostname}/books
```
Add book
```
POST http://{hostname}/books
```
Get selected books information
```
GET http://{hostname}/books/{barcode}
```
Update selected book information
```
PUT http://{hostname}/books/{barcode}
```
Get total price of all books with the same barcode
```
GET http://{hostname}/books/{barcode}/price
```


### Examples
Regular book
```json
{
    "name": "Book3",
    "author": "author3",
    "barcode": "3",
    "quantity": 2,
    "price": 58.0
}
```

Antique book
```json
{
    "name": "Book2",
    "author": "Author2",
    "barcode": "2",
    "quantity": 4,
    "price": 50.0,
    "releaseYear": 1500
}
```

Scientific journal
```json
{
    "name": "Book4",
    "author": "Author4",
    "barcode": "4",
    "quantity": 4,
    "price": 580.0,
    "scienceIndex": 2
}
```

<pre>
GET <a href="https://rbooksapijava.azurewebsites.net/books" target="_blank">https://rbooksapijava.azurewebsites.net/books</a>
</pre>

```json
{
    "2": [
        {
            "name": "Book3",
            "author": "author3",
            "barcode": "3",
            "quantity": 2,
            "price": 58.0
        }
    ],
    "4": [
        {
            "name": "Book4",
            "author": "Author4",
            "barcode": "4",
            "quantity": 4,
            "price": 580.0,
            "scienceIndex": 2
        },
        {
            "name": "Book2",
            "author": "Author2",
            "barcode": "2",
            "quantity": 4,
            "price": 50.0,
            "releaseYear": 1500
        }
    ],
    "8": [
        {
            "name": "Book1",
            "author": "Author1",
            "barcode": "1",
            "quantity": 8,
            "price": 20.0
        }
    ]
}
```

<pre>
GET <a href="https://booksapijava.azurewebsites.net/books/3" target="_blank">https://booksapijava.azurewebsites.net/books/3</a>
</pre>

```json
{
    "name": "Book3",
    "author": "author3",
    "barcode": "3",
    "quantity": 2,
    "price": 58.0
}
```

<pre>
GET <a href="https://booksapijava.azurewebsites.net/books/3/price" target="_blank">https://booksapijava.azurewebsites.net/books/3/price</a>
</pre>

```json
{
    "price": 116.00
}
```


## Development

### Requirements
* Java 11
* Maven
* Tomcat 9


