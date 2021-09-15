CREATE TABLE public.agent (
    id integer NOT NULL,
    name text NOT NULL
);


CREATE TABLE public.photo (
    id integer NOT NULL,
    url text NOT NULL,
    legend text NOT NULL,
    property_id integer NOT NULL
);


CREATE TABLE public.category (
    id integer NOT NULL,
    name text NOT NULL
);


CREATE TABLE public.type (
    id integer NOT NULL,
    name text NOT NULL
);


COMMENT ON TABLE public.type
    IS '
';

CREATE TABLE public.property (
    id integer NOT NULL,
    price integer NOT NULL,
    surface integer NOT NULL,
    description text NOT NULL,
    address text NOT NULL,
    point_of_interest text NOT NULL,
    available boolean NOT NULL,
    entry_date date NOT NULL,
    sale_date timestamp with time zone NOT NULL,
    type_id integer NOT NULL,
    category_id integer NOT NULL,
    agent_id integer NOT NULL
);

