package com.example.arogyanidhi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

enum class Tab { QUIZ, RESULTS, HOSPITALS }

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme(
                colorScheme = lightColorScheme(
                    primary = Color(0xFF059669), // Emerald 600
                    background = Color(0xFFF8FAFC) // Slate 50
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    AppScreen()
                }
            }
        }
    }
}

@Composable
fun AppScreen(viewModel: ArogyaViewModel = viewModel()) {
    var activeTab by remember { mutableStateOf(Tab.QUIZ) }
    var step by remember { mutableStateOf(0) }
    var hospitalSearchTxt by remember { mutableStateOf("") }
    
    val quizData by viewModel.quizData.collectAsState()
    val eligibleSchemes by viewModel.eligibleSchemes.collectAsState()

    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar()
        
        NavigationTabs(
            activeTab = activeTab,
            onTabSelected = { tab ->
                activeTab = tab
                if (tab == Tab.QUIZ) {
                    step = 0
                    viewModel.resetQuiz()
                }
            },
            resultsEnabled = eligibleSchemes.isNotEmpty() || step == 4
        )

        Box(modifier = Modifier
            .weight(1f)
            .padding(16.dp)) {
            when (activeTab) {
                Tab.QUIZ -> QuizScreen(
                    step = step,
                    quizData = quizData,
                    onStepChange = { newStep -> 
                        if (newStep == 4) {
                            viewModel.calculateSchemes()
                            activeTab = Tab.RESULTS
                        } else {
                            step = newStep
                        }
                    },
                    onFieldUpdate = { field, value -> viewModel.updateQuizField(field, value) }
                )
                Tab.RESULTS -> ResultsScreen(
                    eligibleSchemes = eligibleSchemes,
                    onUpdateProfile = {
                        activeTab = Tab.QUIZ
                        step = 0
                        viewModel.resetQuiz()
                    },
                    onViewHospitals = {
                        activeTab = Tab.HOSPITALS
                        hospitalSearchTxt = quizData.district
                    }
                )
                Tab.HOSPITALS -> HospitalsScreen(
                    searchTxt = hospitalSearchTxt,
                    onSearchChange = { hospitalSearchTxt = it },
                    eligibleSchemes = eligibleSchemes
                )
            }
        }
    }
}

@Composable
fun TopAppBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF059669)),
            contentAlignment = Alignment.Center
        ) {
            Text("A", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 24.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text("Arogya-Nidhi", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            Text("HEALTH SCHEME COUNSELOR", fontSize = 10.sp, color = Color.Gray)
        }
    }
}

@Composable
fun NavigationTabs(activeTab: Tab, onTabSelected: (Tab) -> Unit, resultsEnabled: Boolean) {
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        TabButton("New Scan", activeTab == Tab.QUIZ, modifier = Modifier.weight(1f)) { onTabSelected(Tab.QUIZ) }
        TabButton("Results", activeTab == Tab.RESULTS, enabled = resultsEnabled, modifier = Modifier.weight(1f)) { onTabSelected(Tab.RESULTS) }
        TabButton("Hospitals", activeTab == Tab.HOSPITALS, modifier = Modifier.weight(1f)) { onTabSelected(Tab.HOSPITALS) }
    }
}

@Composable
fun TabButton(text: String, selected: Boolean, enabled: Boolean = true, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val bgColor = if (selected) Color(0xFF059669) else Color.White
    val contentColor = if (selected) Color.White else Color.DarkGray
    
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(48.dp),
        colors = ButtonDefaults.buttonColors(containerColor = bgColor, disabledContainerColor = Color.LightGray),
        shape = RoundedCornerShape(12.dp)
    ) {
        Text(text, color = contentColor, fontWeight = FontWeight.Bold)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuizScreen(step: Int, quizData: QuizState, onStepChange: (Int) -> Unit, onFieldUpdate: (String, String) -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(24.dp)) {
            
            // Stepper Visual
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                for (i in 0..3) {
                    val isActive = i == step
                    val isCompleted = i < step
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(if (isActive || isCompleted) Color(0xFF059669) else Color(0xFFE2E8F0)),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
                        } else {
                            Text("${i + 1}", color = if (isActive) Color.White else Color.Gray, fontWeight = FontWeight.Bold)
                        }
                    }
                    if (i < 3) {
                        Divider(modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp), color = if (isCompleted) Color(0xFF059669) else Color(0xFFE2E8F0), thickness = 2.dp)
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Step Content
            Column(modifier = Modifier.weight(1f)) {
                when (step) {
                    0 -> {
                        Text("Select Your District", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Where does your family reside?", color = Color.Gray, modifier = Modifier.padding(bottom = 24.dp, top = 8.dp))
                        
                        var expanded by remember { mutableStateOf(false) }
                        ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                            OutlinedTextField(
                                value = quizData.district.ifEmpty { "-- SELECT AREA --" },
                                onValueChange = {},
                                readOnly = true,
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                modifier = Modifier
                                    .menuAnchor()
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                                LocalData.DISTRICTS.forEach { selection ->
                                    DropdownMenuItem(
                                        text = { Text(selection) },
                                        onClick = {
                                            onFieldUpdate("district", selection)
                                            expanded = false
                                        }
                                    )
                                }
                            }
                        }
                    }
                    1 -> {
                        Text("Family Financial Profile", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("This helps us determine eligibility for State and Central schemes.", color = Color.Gray, modifier = Modifier.padding(bottom = 24.dp, top = 8.dp))
                        OutlinedTextField(
                            value = quizData.income,
                            onValueChange = { onFieldUpdate("income", it) },
                            modifier = Modifier.fillMaxWidth(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            placeholder = { Text("e.g. 15000") },
                            leadingIcon = { Text("₹") }
                        )
                    }
                    2 -> {
                        Text("Below Poverty Line Status", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("Does your family have a BPL (Below Poverty Line) card?", color = Color.Gray, modifier = Modifier.padding(bottom = 24.dp, top = 8.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                            QuizSelectionButton("Yes", quizData.bpl == "Yes", modifier = Modifier.weight(1f)) { onFieldUpdate("bpl", "Yes") }
                            QuizSelectionButton("No", quizData.bpl == "No", modifier = Modifier.weight(1f)) { onFieldUpdate("bpl", "No") }
                        }
                    }
                    3 -> {
                        Text("Occupation Details", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                        Text("What is the primary occupation of the breadwinner?", color = Color.Gray, modifier = Modifier.padding(bottom = 24.dp, top = 8.dp))
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(LocalData.OCCUPATIONS) { occ ->
                                QuizSelectionButton(occ, quizData.occupation == occ, modifier = Modifier.fillMaxWidth()) {
                                    onFieldUpdate("occupation", occ)
                                }
                            }
                        }
                    }
                }
            }
            
            Divider(modifier = Modifier.padding(vertical = 16.dp))
            
            // Navigation Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                TextButton(onClick = { onStepChange(step - 1) }, enabled = step > 0) {
                    Text("Back")
                }
                
                val isNextEnabled = when(step) {
                    0 -> quizData.district.isNotEmpty()
                    1 -> quizData.income.isNotEmpty()
                    2 -> quizData.bpl.isNotEmpty()
                    3 -> quizData.occupation.isNotEmpty()
                    else -> true
                }
                
                Button(onClick = { onStepChange(step + 1) }, enabled = isNextEnabled) {
                    Text(if (step == 3) "Analyze Data" else "Continue")
                }
            }
        }
    }
}

@Composable
fun QuizSelectionButton(text: String, selected: Boolean, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val borderColor = if (selected) Color(0xFF059669) else Color(0xFFE2E8F0)
    val bgColor = if (selected) Color(0xFFECFDF5) else Color.White
    val textColor = if (selected) Color(0xFF047857) else Color.DarkGray
    
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .background(bgColor)
            .clickable { onClick() }
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = textColor, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun ResultsScreen(eligibleSchemes: List<Scheme>, onUpdateProfile: () -> Unit, onViewHospitals: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (eligibleSchemes.isEmpty()) {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text("No Matches Found", fontSize = 24.sp, fontWeight = FontWeight.Bold)
                Text("Based on the provided information, we couldn't match you with any active schemes.", color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(vertical = 16.dp))
                OutlinedButton(onClick = onUpdateProfile) { Text("Update Profile") }
            }
        } else {
            Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
                Text("Eligibility Results", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color(0xFF059669))
                Text("You are eligible for ${eligibleSchemes.size} scheme(s).", modifier = Modifier.padding(vertical = 16.dp))
                
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    items(eligibleSchemes) { scheme ->
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAFC)),
                            border = borderStroke(),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(scheme.name, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                Text(scheme.description, color = Color.DarkGray, fontSize = 14.sp, modifier = Modifier.padding(vertical = 8.dp))
                                Divider(modifier = Modifier.padding(vertical = 8.dp))
                                Text("Required Documents:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = Color.Gray)
                                scheme.documents.forEach { doc ->
                                    Text("• $doc", fontSize = 14.sp, color = Color.DarkGray)
                                }
                            }
                        }
                    }
                }
                
                Button(onClick = onViewHospitals, modifier = Modifier.fillMaxWidth().padding(top = 16.dp)) {
                    Text("View Eligible Hospitals")
                }
            }
        }
    }
}

@Composable
private fun borderStroke() = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HospitalsScreen(searchTxt: String, onSearchChange: (String) -> Unit, eligibleSchemes: List<Scheme>) {
    val filteredHospitals = LocalData.HOSPITALS.filter { searchTxt.isEmpty() || it.district.contains(searchTxt, ignoreCase = true) }
    
    Card(
        modifier = Modifier.fillMaxSize(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Slate 900
    ) {
        Column(modifier = Modifier.fillMaxSize().padding(24.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Nearby Hospitals", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
                Badge(containerColor = Color(0xFF10B981)) { 
                    Text("${filteredHospitals.size} Found", modifier = Modifier.padding(4.dp), color = Color.White) 
                }
            }
            
            OutlinedTextField(
                value = searchTxt,
                onValueChange = onSearchChange,
                modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                placeholder = { Text("Search District...", color = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color(0xFF1E293B),
                    focusedContainerColor = Color(0xFF1E293B),
                    unfocusedTextColor = Color.White,
                    focusedTextColor = Color.White,
                    unfocusedBorderColor = Color.Transparent,
                    focusedBorderColor = Color(0xFF10B981)
                ),
                shape = RoundedCornerShape(12.dp)
            )
            
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                items(filteredHospitals) { hospital ->
                    val supportedEligible = eligibleSchemes.filter { hospital.schemes.contains(it.id) }
                    
                    Column(modifier = Modifier.fillMaxWidth().border(borderStroke()).padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                            Text(hospital.name, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(hospital.type, color = if(hospital.type == "Private") Color(0xFF60A5FA) else Color(0xFF34D399), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(hospital.district, color = Color.Gray, fontSize = 14.sp)
                        
                        if (supportedEligible.isNotEmpty()) {
                            Row(modifier = Modifier.padding(top = 8.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                supportedEligible.forEach { s ->
                                    Box(modifier = Modifier.background(Color(0xFF1E293B), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)) {
                                        Text(s.name.split(" ")[0], color = Color(0xFF34D399), fontSize = 10.sp)
                                    }
                                }
                            }
                        } else {
                            Text("Total Schemes Supported: ${hospital.schemes.size}", color = Color.Gray, fontSize = 12.sp, modifier = Modifier.padding(top = 8.dp))
                        }
                    }
                }
            }
        }
    }
}
