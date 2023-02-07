CREATE TABLE IF NOT EXISTS USERS
(
    ID LONG AUTO_INCREMENT,
    EMAIL CHARACTER VARYING(45) UNIQUE NOT NULL,
    NAME CHARACTER VARYING(45) NOT NULL,
    CONSTRAINT "USERS_PK" PRIMARY KEY (ID)
);

CREATE TABLE IF NOT EXISTS REQUESTS
(
    ID LONG AUTO_INCREMENT,
    DESCRIPTION CHARACTER VARYING(400),
    REQUESTER_ID LONG NOT NULL,
    CONSTRAINT "REQUEST_PK" PRIMARY KEY(ID),
    CONSTRAINT "USER_REQ_FK" FOREIGN KEY(REQUESTER_ID) REFERENCES USERS
);

CREATE TABLE IF NOT EXISTS ITEMS
(
    ID LONG AUTO_INCREMENT,
    NAME CHARACTER VARYING(40) NOT NULL,
    DESCRIPTION CHARACTER VARYING(400),
    IS_AVAILABLE BOOLEAN,
    OWNER_ID LONG NOT NULL,
    REQUEST_ID LONG,
    CONSTRAINT "ITEM_PK" PRIMARY KEY(ID),
    CONSTRAINT "USER_ITEM_FK" FOREIGN KEY(OWNER_ID) REFERENCES USERS,
    CONSTRAINT "REQUEST_iTEM_FK" FOREIGN KEY(REQUEST_ID) REFERENCES REQUESTS
);

CREATE TABLE IF NOT EXISTS BOOKINGS
(
    ID LONG AUTO_INCREMENT,
    START_DATE TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    END_DATE TIMESTAMP WITHOUT TIME ZONE,
    ITEM_ID LONG NOT NULL,
    BOOKER_ID LONG NOT NULL,
    STATUS CHARACTER VARYING(20) NOT NULL,
    CONSTRAINT "BOOKING_PK" PRIMARY KEY(ID),
    CONSTRAINT "ITEM_FK" FOREIGN KEY(ITEM_ID) REFERENCES ITEMS,
    CONSTRAINT "USER_BOOKING_FK" FOREIGN KEY(BOOKER_ID) REFERENCES USERS
);


CREATE TABLE IF NOT EXISTS COMMENTS
(
    ID LONG AUTO_INCREMENT,
    DESCRIPTION CHARACTER VARYING(400),
    ITEM_ID LONG NOT NULL,
    AUTHOR_ID LONG NOT NULL,
    CREATED TIMESTAMP WITHOUT TIME ZONE NOT NULL,
    CONSTRAINT "COMMENT_PK" PRIMARY KEY(ID),
    CONSTRAINT "ITEM_COMMENT_FK" FOREIGN KEY(ITEM_ID) REFERENCES ITEMS,
    CONSTRAINT "USER_COMMENT_FK" FOREIGN KEY(AUTHOR_ID) REFERENCES USERS
);