db.createUser({
    user: 'developer',
    pwd: 'Develop3r',
    roles: [{
        role: 'readWrite',
        db: 'ginormitron'
    }]
})
