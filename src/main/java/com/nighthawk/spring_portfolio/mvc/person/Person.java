package com.nighthawk.spring_portfolio.mvc.person;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Convert;
import static jakarta.persistence.FetchType.EAGER;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.format.annotation.DateTimeFormat;

import com.vladmihalcea.hibernate.type.json.JsonType;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

/*
Person is a POJO, Plain Old Java Object.
First set of annotations add functionality to POJO
--- @Setter @Getter @ToString @NoArgsConstructor @RequiredArgsConstructor
The last annotation connect to database
--- @Entity
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Convert(attributeName ="person", converter = JsonType.class)
public class Person {

    // automatic unique identifier for Person record
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    // email, password, roles are key attributes to login and authentication
    @NotEmpty
    @Size(min=5)
    @Column(unique=true)
    @Email
    private String email;

    @NotEmpty
    private String password;

    // @NonNull, etc placed in params of constructor: "@NonNull @Size(min = 2, max = 30, message = "Name (2 to 30 chars)") String name"
    @NonNull
    @Size(min = 2, max = 30, message = "Name (2 to 30 chars)")
    private String name;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @NonNull
    @Size(min = 2, max = 20, message = "Username (Min of 2 and Max of 20 characters)")
    private String username;

    // To be implemented
    @ManyToMany(fetch = EAGER)
    private Collection<PersonRole> roles = new ArrayList<>();

    /* HashMap is used to store JSON for daily "stats"
    "stats": {
        "2022-11-13": {
            "calories": 2200,
            "steps": 8000
        }
    }
    */
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String,Map<String, Object>> stats = new HashMap<>(); 
    

    // Constructor used when building object from an API
    public Person(String email, String password, String name, Date dob, String username, PersonRole role) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.dob = dob;
        this.username = username;
        this.roles.add(role);
    }

    // A custom getter to return age from dob attribute
    public int getAge() {
        if (this.dob != null) {
            LocalDate birthDay = this.dob.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
            return Period.between(birthDay, LocalDate.now()).getYears(); }
        return -1;
    }

    public static Person createPerson(String name, String email, String password, String dob, String username) {
        // By default, Spring Security expects roles to have a "ROLE_" prefix.
        return createPerson(name, email, password, dob, username, Arrays.asList("ROLE_USER"));
    }

    public static Person createPerson(String name, String email, String password, String dob, String username, List<String> roleNames) {
        Person person = new Person();
        person.setName(name);
        person.setEmail(email);
        person.setPassword(password);
        try {
            Date date = new SimpleDateFormat("MM-dd-yyyy").parse(dob);
            person.setDob(date);
        } catch (Exception e) {
            // handle exception
        }
        person.setUsername(username);
    
        List<PersonRole> roles = new ArrayList<>();
        for (String roleName : roleNames) {
            PersonRole role = new PersonRole(roleName);
            roles.add(role);
        }
        person.setRoles(roles);
    
        return person;
    }

    // Initialize static test data 
    public static Person[] init() {
        ArrayList<Person> persons = new ArrayList<>();
        persons.add(createPerson("Tay Kim", "tay@gmail.com", "taykimmy123", "05-13-2007", "TayKimmy", Arrays.asList("ROLE_ADMIN", "ROLE_USER"))); 
        persons.add(createPerson("Anthony Bazhenov", "ant@gmail.com", "ant11234123", "01-12-2007", "Ant11234", Arrays.asList("ROLE_ADMIN", "ROLE_USER"))); 
        persons.add(createPerson("Ethan Tran", "ethan@gmail.com", "realethantran123", "05-19-2007", "realethantran", Arrays.asList("ROLE_ADMIN", "ROLE_USER"))); 
        persons.add(createPerson("Emaad Mir", "emaad@gmail.com", "emaad-mir123", "01-12-2007", "Emaad-Mir", Arrays.asList("ROLE_ADMIN", "ROLE_USER"))); 
        persons.add(createPerson("John Mortensen", "jm1021@gmail.com", "123Qwerty!", "10-21-1959", "jmort29", Arrays.asList("ROLE_ADMIN", "ROLE_USER"))); 
        persons.add(createPerson("Thomas Edison", "toby@gmail.com", "123Toby!", "07-08-2001", "toby123", Arrays.asList("ROLE_USER")));
        
        // Array definition and data initialization
        return persons.toArray(new Person[0]);
    }

    public static void main(String[] args) {
        // obtain Person from initializer
        Person persons[] = init();

        // iterate using "enhanced for loop"
        for( Person person : persons) {
            System.out.println(person);  // print object
        }
    }

}