-- V1: esquema base generado por Hibernate desde las entidades JPA (2026-10-06).
-- BDs existentes (creadas con ddl-auto=update) se baselinean en V1 y no re-ejecutan este script.
-- Cambios futuros: nuevos archivos V2__*.sql, V3__*.sql ... (nunca editar este).

create table customers (active boolean not null, credit_limit numeric(12,2), pending_balance numeric(12,2), created_at timestamp(6) not null, id bigserial not null, updated_at timestamp(6), postal_code varchar(10), phone varchar(20), code varchar(50) not null unique, customer_type varchar(50), document_number varchar(50) unique, document_type varchar(50), city varchar(100), email varchar(100) unique, name varchar(100) not null, address varchar(255), primary key (id));

create table goods_receipt_items (ordered_quantity integer not null, received_quantity integer, unit_price numeric(19,2), goods_receipt_id bigint not null, id bigserial not null, product_id bigint not null, product_code varchar(50) not null, notes varchar(500), product_name varchar(255) not null, primary key (id));

create table goods_receipts (actual_delivery_date timestamp(6), created_at timestamp(6) not null, expected_delivery_date timestamp(6), id bigserial not null, order_id bigint not null, receipt_date timestamp(6), supplier_id bigint, updated_at timestamp(6), status varchar(50) not null, supplier_code varchar(50), order_number varchar(100), receipt_number varchar(100) not null unique, notes varchar(1000), supplier_name varchar(255), primary key (id));

create table notifications (is_read boolean not null, created_at timestamp(6) not null, id bigserial not null, read_at timestamp(6), title varchar(500) not null, message TEXT not null, related_entity_id varchar(255), related_entity_type varchar(255), required_role varchar(255) not null, type varchar(255) not null check (type in ('INVENTORY_ALERT','ORDER_ALERT','SYSTEM_ALERT','USER_ALERT')), primary key (id));

create table order_items (quantity integer not null, subtotal numeric(14,2), unit_price numeric(12,2) not null, created_at timestamp(6), id bigserial not null, order_id bigint not null, product_id bigint not null, updated_at timestamp(6), product_code varchar(50), product_name varchar(100), primary key (id));

create table orders (total numeric(14,2), actual_delivery_date timestamp(6), created_at timestamp(6) not null, customer_id bigint not null, expected_delivery_date timestamp(6), id bigserial not null, order_date timestamp(6), supplier_id bigint, updated_at timestamp(6), status varchar(20) not null, customer_code varchar(50), order_number varchar(50) not null unique, supplier_code varchar(50), created_by varchar(100), customer_name varchar(100), supplier_name varchar(100), notes TEXT, primary key (id));

create table password_reset_tokens (used boolean not null, created_at timestamp(6) not null, expiry_date timestamp(6) not null, id bigserial not null, user_id bigint not null, token varchar(100) not null unique, primary key (id));

create table payments (amount numeric(38,2) not null, created_at timestamp(6) not null, customer_id bigint not null, id bigserial not null, order_id bigint not null, paid_at timestamp(6), updated_at timestamp(6), currency varchar(255) not null, description varchar(255), error_message varchar(255), payment_method varchar(255), status varchar(255) not null check (status in ('PENDING','PROCESSING','SUCCEEDED','FAILED','DECLINED','CANCELLED','REFUNDED')), stripe_intent_id varchar(255) unique, stripe_payment_id varchar(255) unique, primary key (id));

create table products (active boolean not null, min_stock integer not null, price numeric(10,2) not null, stock integer not null, created_at timestamp(6) not null, id bigserial not null, updated_at timestamp(6), code varchar(50) not null unique, category varchar(100), name varchar(200) not null, description varchar(500), primary key (id));

create table suppliers (active boolean not null, average_payment_delay numeric(5,2), lead_time_days integer, created_at timestamp(6) not null, id bigserial not null, updated_at timestamp(6), postal_code varchar(10), phone varchar(20), code varchar(50) not null unique, document_number varchar(50), document_type varchar(50), city varchar(100), email varchar(100) unique, name varchar(100) not null, address varchar(255), primary key (id));

create table users (active boolean not null, email_verified boolean not null, created_at timestamp(6) not null, id bigserial not null, last_login timestamp(6), updated_at timestamp(6), role varchar(20) not null check (role in ('ADMIN','MANAGER','SALES','WAREHOUSE','USER')), username varchar(50) not null unique, email varchar(100) not null unique, email_verification_token varchar(100), first_name varchar(100), last_name varchar(100), password varchar(255) not null, primary key (id));

create index idx_active on customers (active);
create index idx_customer_type on customers (customer_type);
create index idx_order_id on goods_receipts (order_id);
create index idx_supplier_id on goods_receipts (supplier_id);
create index idx_status on goods_receipts (status);
create index idx_customer_id on orders (customer_id);
create index idx_orders_status on orders (status);
create index idx_order_date on orders (order_date);
create index idx_payment_order on payments (order_id);
create index idx_payment_customer on payments (customer_id);
create index idx_payment_status on payments (status);
create index idx_payment_stripe_id on payments (stripe_payment_id);
create index idx_payment_created on payments (created_at);
create index idx_supplier_active on suppliers (active);
create index idx_users_active on users (active);
