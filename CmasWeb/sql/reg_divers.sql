select * from divers where not (firstName like '%Bot')
                                    and role = 'ROLE_DIVER'
                                    and id > 100
                                    and email <> 'ekaterina_petoukhova@hotmail.com'
                                    and password is not null
