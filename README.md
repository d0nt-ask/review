```sql
CREATE USER 'product'@'%' IDENTIFIED BY 'product';
GRANT Alter ON product.* TO 'product'@'%';
GRANT Create ON product.* TO 'product'@'%';
GRANT Delete ON product.* TO 'product'@'%';
GRANT Drop ON product.* TO 'product'@'%';
GRANT Insert ON product.* TO 'product'@'%';
GRANT Select ON product.* TO 'product'@'%';
GRANT Update ON product.* TO 'product'@'%';
GRANT Lock tables ON product.* TO 'product'@'%';
           
CREATE USER 'order'@'%' IDENTIFIED BY 'order';
GRANT Alter ON `order`.* TO 'order'@'%';
GRANT Create ON `order`.* TO 'order'@'%';
GRANT Delete ON `order`.* TO 'order'@'%';
GRANT Drop ON `order`.* TO 'order'@'%';
GRANT Insert ON `order`.* TO 'order'@'%';
GRANT Select ON `order`.* TO 'order'@'%';
GRANT Update ON `order`.* TO 'order'@'%';
GRANT Lock tables ON `order`.* TO 'order'@'%';
```