## CheckList
### Product 
- [x] Product 기본 설계
- [x] API - getProduct
- [x] API - getProductsByPagination
- [x] API - addProduct
- [x] API - updateProduct
- [x] API - deleteProduct
---
### Order
- [ ] Order 기본 설계 - 보완 필요
- [ ] API - getOrders - 진행중
- [x] API - getOrder - 진행중
- [x] API - orderProduct - 동시성 처리
- [ ] API - changeOrder
- [ ] API - deleteOrder
---
### 공통
- [ ] 입력 검증
- [ ] 예외 처리
- [ ] 설정 분리


## DB
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