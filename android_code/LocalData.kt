package com.example.arogyanidhi

object LocalData {
    val SCHEMES = listOf(
        Scheme(
            id = "s1",
            name = "Ayushman Bharat (PM-JAY)",
            description = "Provides health cover of Rs. 5 lakhs per family per year for secondary and tertiary care hospitalization.",
            documents = listOf("Aadhar Card", "Rashan Card", "PMJAY Letter / Golden Card"),
            eligibility = Eligibility(bplRequired = true)
        ),
        Scheme(
            id = "s2",
            name = "Rashtriya Swasthya Bima Yojana (RSBY)",
            description = "Health insurance scheme for Below Poverty Line (BPL) families in the unorganized sector.",
            documents = listOf("BPL Card", "Aadhar Card", "Passport Size Photo"),
            eligibility = Eligibility(bplRequired = true, occupations = listOf("Unorganized Worker", "Farmer", "Daily Wage"))
        ),
        Scheme(
            id = "s3",
            name = "Chief Minister's Comprehensive Health Insurance",
            description = "State-specific health protection scheme for low-income families.",
            documents = listOf("Income Certificate", "Aadhar Card", "Family ID (Ration Card)"),
            eligibility = Eligibility(maxIncome = 120000)
        ),
        Scheme(
            id = "s4",
            name = "Employee State Insurance (ESI)",
            description = "Social security and health insurance scheme for Indian workers.",
            documents = listOf("ESI e-Pehchan Card", "Aadhar Card", "Employment Proof"),
            eligibility = Eligibility(maxIncome = 252000, occupations = listOf("Salaried"))
        )
    )

    val HOSPITALS = listOf(
        Hospital("h1", "District General Hospital", "Mumbai", listOf("s1", "s2", "s3"), "Public"),
        Hospital("h2", "Sanjeevani Clinic", "Mumbai", listOf("s1", "s4"), "Private"),
        Hospital("h3", "City Care Hospital", "Delhi", listOf("s1", "s2", "s3", "s4"), "Private"),
        Hospital("h4", "Government Medical College", "Delhi", listOf("s1", "s2", "s3"), "Public"),
        Hospital("h5", "Rural Health Center", "Pune", listOf("s1", "s2", "s3"), "Public"),
        Hospital("h6", "Apex Medicore", "Pune", listOf("s1", "s4"), "Private"),
        Hospital("h7", "Bangalore General", "Bangalore", listOf("s1", "s2", "s3"), "Public"),
        Hospital("h8", "Chennai Health Trust", "Chennai", listOf("s1", "s3", "s4"), "Public")
    )

    val OCCUPATIONS = listOf(
        "Farmer", "Daily Wage", "Unorganized Worker", "Salaried", "Self-Employed", "Unemployed"
    )

    val DISTRICTS = listOf("Mumbai", "Delhi", "Pune", "Bangalore", "Chennai", "Kolkata", "Hyderabad")
}
