package com.felysoft.felysoftApp.util;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum Role {
    ADMINISTRATOR(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_ONE_PRODUCT,
            Permission.CREATE_ONE_PRODUCT,
            Permission.UPDATE_ONE_PRODUCT,
            Permission.DISABLE_ONE_PRODUCT,
            Permission.READ_INVENTORY_PRODUCTS,

            Permission.READ_ALL_CATEGORIES,
            Permission.READ_ONE_CATEGORY,
            Permission.CREATE_ONE_CATEGORY,
            Permission.UPDATE_ONE_CATEGORY,
            Permission.DISABLE_ONE_CATEGORY,
            Permission.READ_CATEGORIES_BY_PROVIDER,
            Permission.ASSOCIATE_CATEGORY_PROVIDER,

            Permission.READ_ALL_PROVIDERS,
            Permission.READ_ONE_PROVIDER,
            Permission.CREATE_ONE_PROVIDER,
            Permission.UPDATE_ONE_PROVIDER,
            Permission.DISABLE_ONE_PROVIDER,
            Permission.READ_PROVIDERS_BY_CATEGORY,

            Permission.READ_ALL_SERVICES,
            Permission.READ_ONE_SERVICE,
            Permission.CREATE_ONE_SERVICE,
            Permission.UPDATE_ONE_SERVICE,
            Permission.DISABLE_ONE_SERVICE,

            Permission.READ_ALL_TYPE_SERVICES,
            Permission.READ_ONE_TYPE_SERVICE,
            Permission.CREATE_ONE_TYPE_SERVICE,
            Permission.UPDATE_ONE_TYPE_SERVICE,
            Permission.DISABLE_ONE_TYPE_SERVICE,

            Permission.READ_ALL_GENRES,
            Permission.READ_ONE_GENRE,
            Permission.CREATE_ONE_GENRE,
            Permission.UPDATE_ONE_GENRE,
            Permission.DISABLE_ONE_GENRE,
            Permission.READ_GENRES_BY_AUTHOR,
            Permission.ASSOCIATE_GENRE_AUTHOR,

            Permission.READ_ALL_AUTHORS,
            Permission.READ_ONE_AUTHOR,
            Permission.CREATE_ONE_AUTHOR,
            Permission.UPDATE_ONE_AUTHOR,
            Permission.DISABLE_ONE_AUTHOR,
            Permission.READ_AUTHORS_BY_GENRE,

            Permission.READ_ALL_BOOKS,
            Permission.READ_ONE_BOOK,
            Permission.CREATE_ONE_BOOK,
            Permission.UPDATE_ONE_BOOK,
            Permission.DISABLE_ONE_BOOK,
            Permission.READ_INVENTORY_BOOKS,

            Permission.READ_ALL_CHARGES,
            Permission.READ_ONE_CHARGE,
            Permission.CREATE_ONE_CHARGE,
            Permission.UPDATE_ONE_CHARGE,
            Permission.DISABLE_ONE_CHARGE,

            Permission.READ_ALL_EMPLOYEES,
            Permission.READ_ONE_EMPLOYEE,
            Permission.CREATE_ONE_EMPLOYEE,
            Permission.UPDATE_ONE_EMPLOYEE,
            Permission.DISABLE_ONE_EMPLOYEE,

            Permission.READ_ALL_PAYMENTS,
            Permission.READ_ONE_PAYMENT,
            Permission.CREATE_ONE_PAYMENT,
            Permission.UPDATE_ONE_PAYMENT,
            Permission.DISABLE_ONE_PAYMENT,

            Permission.READ_ALL_DETAILS,
            Permission.READ_ONE_DETAIL,
            Permission.CREATE_ONE_DETAIL,
            Permission.UPDATE_ONE_DETAIL,
            Permission.DISABLE_ONE_DETAIL,

            Permission.READ_ALL_EXPENSES,
            Permission.READ_ONE_EXPENSE,
            Permission.CREATE_ONE_EXPENSE,
            Permission.UPDATE_ONE_EXPENSE,
            Permission.DISABLE_ONE_EXPENSE,

            Permission.READ_ALL_SALES,
            Permission.READ_ONE_SALE,
            Permission.CREATE_ONE_SALE,
            Permission.UPDATE_ONE_SALE,
            Permission.DISABLE_ONE_SALE,

            Permission.READ_ALL_RESERVES,
            Permission.READ_ONE_RESERVE,
            Permission.CREATE_ONE_RESERVE,
            Permission.UPDATE_ONE_RESERVE,
            Permission.DISABLE_ONE_RESERVE,

            Permission.READ_ALL_USERS,
            Permission.READ_ONE_USER,
            Permission.CREATE_ONE_USER,
            Permission.UPDATE_ONE_USER,
            Permission.DISABLE_ONE_USER,

            Permission.READ_ALL_PURCHASES,
            Permission.READ_ONE_PURCHASE,
            Permission.CREATE_ONE_PURCHASE,
            Permission.UPDATE_ONE_PURCHASE,
            Permission.DISABLE_ONE_PURCHASE,
            Permission.READ_EXPENSE_BY_PURCHASE, //

            Permission.READ_MY_PROFILE
    )),
    CUSTOMER(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_MY_PROFILE
    )),
    USER(Arrays.asList(
            Permission.READ_ALL_PRODUCTS,
            Permission.READ_MY_PROFILE
    ));

    private final List<Permission> permissions;
}
