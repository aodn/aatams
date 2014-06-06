--
-- This script simply generates an admin user on the blank DB
--

-- Edit the values before running it
--

-- Create a SysAdmin role
insert into aatams.sec_role (id, version, name) values (0, 0, 'SysAdmin');

-- Create an address
insert into aatams.address
    (id, version, country, postcode, state, street_address, suburb_town)
    values
    (0, 0, 'Australia', '7000', 'TAS', 'St. Address', 'Hobart');

-- Create an organization
insert into aatams.organisation (
    id, version, department, name,
    phone_number, postal_address_id, street_address_id, status)
    values
    (0, 0, 'IMOS', 'IMOS',
    '+61-4-11111111', 0, 0, 'ACTIVE');

-- Create the user
-- Password is "password", obtain a password by running:
-- echo -n 'password' | sha256sum
-- Given password here is for 'password'
-- The long string will set the timezone to Australia/Melbourne
insert into aatams.sec_user
    (id, version, password_hash,
    username, class, default_time_zone,
    email_address, name,
    organisation_id, phone_number,
    registration_comment, status)
    values
    (0, 0, '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8',
    'admin', 'au.org.emii.aatams.Person',
    '\xaced00057372001f6f72672e6a6f64612e74696d652e4461746554696d655a6f6e652453747562a62f019a7c321ae30300007870771500134175737472616c69612f4d656c626f75726e6578',
    'your@email.com', 'Sys Admin',
    0, '+61-4-11111111', 'Sys Admin User', 'ACTIVE');

-- Associate user with SysAdmin role
insert into aatams.sec_user_roles (sec_role_id, sec_user_id) values (0, 0);

-- Grant user all permissions
insert into aatams.sec_role_permissions (sec_role_id, permissions_string) values (0, '*:*');
