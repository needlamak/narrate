package com.example.narrate

// Add these imports
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.content.MediaType
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp


// State holder for the chat UI
//data class ChatUiState(
//    val isBottomRowExpanded: Boolean = false,
//    val isTextListVisible: Boolean = false,
//    val isUploading: Boolean = false,
//    val currentText: String = "",
//    val audioTitle: String = "",
//    val isAudioPlaying: Boolean = false
//)

@Preview(showSystemUi = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen() {
    var uiState by remember { mutableStateOf(ChatUiState()) }
    val bottomSheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()) {
        // Main content columns
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = if (uiState.isBottomRowExpanded) 200.dp else 80.dp)
        ) {
            // User columns
            UserColumn(modifier = Modifier.weight(1f))
            UserColumn(modifier = Modifier.weight(1f))
        }

        // Bottom controls
        BottomControls(
            uiState = uiState,
            onStateChange = { uiState = it },
            modifier = Modifier.align(Alignment.BottomCenter)
        )

        // Text list bottom sheet
        if (uiState.isTextListVisible) {
            TextListBottomSheet(
                onDismiss = { uiState = uiState.copy(isTextListVisible = false) }
            )
        }
    }
}

@Composable
fun UserColumn(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(8.dp)
    ) {
        // Audio card at top
        AudioCard()

        // Content area (text, images, videos)
        ContentArea()
    }
}

@Composable
fun AudioCard() {
    var isExpanded by remember { mutableStateOf(false) }

    AnimatedVisibility(
        visible = isExpanded,
        enter = expandHorizontally(),
        exit = shrinkHorizontally()
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(4.dp)
        ) {
            Row(
                modifier = Modifier.padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Audio Title")
            }
        }
    }
}

@Composable
fun ContentArea() {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .transformable(
                state = rememberTransformableState { zoomChange, offsetChange, _ ->
                    scale *= zoomChange
                    offset += offsetChange
                }
            )
    ) {
        // Content items (text, images, videos)
        Column(
            modifier = Modifier
                .scale(scale)
                .offset { IntOffset(offset.x.toInt(), offset.y.toInt()) }
        ) {
            // Content items go here
        }
    }
}

@Composable
fun BottomControls(
    uiState: ChatUiState,
    onStateChange: (ChatUiState) -> Unit,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = true,
        enter = slideInVertically { it },
        exit = slideOutVertically { it },
        modifier = modifier
    ) {
        Column {
            // Text list button
            TextListButton(
                isVisible = !uiState.isBottomRowExpanded,
                onClick = { onStateChange(uiState.copy(isTextListVisible = true)) }
            )

            // Bottom row with buttons
            BottomRow(
                isExpanded = uiState.isBottomRowExpanded,
                currentText = uiState.currentText,
                onTextChange = { onStateChange(uiState.copy(currentText = it)) },
                onExpandChange = { onStateChange(uiState.copy(isBottomRowExpanded = it)) }
            )
        }
    }
}

@Composable
fun TextListButton(
    isVisible: Boolean,
    onClick: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
        ) {
            Text("Text List")
        }
    }
}

@Composable
fun BottomRow(
    isExpanded: Boolean,
    currentText: String,
    onTextChange: (String) -> Unit,
    onExpandChange: (Boolean) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .animateContentSize()
    ) {
        if (isExpanded) {
            // Expanded content
            ExpandedControls(
                currentText = currentText,
                onTextChange = onTextChange,
                onCollapse = { onExpandChange(false) }
            )
        } else {
            // Collapsed buttons
            CollapsedControls(
                onExpand = { onExpandChange(true) }
            )
        }
    }
}

@Composable
fun ExpandedControls(
    currentText: String,
    onTextChange: (String) -> Unit,
    onCollapse: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        TextField(
            value = currentText,
            onValueChange = {
                if (it.split(" ").size <= 3) {
                    onTextChange(it)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            IconButton(onClick = { /* Handle media upload */ }) {
                Icon(Icons.Default.Add, "Upload Media")
            }
            IconButton(onClick = { /* Handle voice recording */ }) {
                Icon(Icons.Default.Add, "Record Voice")
            }
            IconButton(onClick = onCollapse) {
                Icon(Icons.Default.Close, "Close")
            }
        }
    }
}

@Composable
fun CollapsedControls(onExpand: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        IconButton(onClick = onExpand) {
            Icon(Icons.Default.Add, "Expand")
        }
        IconButton(onClick = onExpand) {
            Icon(Icons.Default.Edit, "Text")
        }
        IconButton(onClick = onExpand) {
            Icon(Icons.Default.Add, "Voice")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextListBottomSheet(onDismiss: () -> Unit) {
    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = rememberModalBottomSheetState()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            // Add text list items here
            items(10) { index ->
                Text(
                    text = "Text item $index",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                )
            }
        }
    }
}

@Composable
fun UploadProgressIndicator(
    isUploading: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = isUploading,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically(),
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(40.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

// First, add these dependencies to your build.gradle
/*
dependencies {
    // Firebase BoM
    implementation platform('com.google.firebase:firebase-bom:32.7.0')

    // Firebase services
    implementation 'com.google.firebase:firebase-firestore-ktx'
    implementation 'com.google.firebase:firebase-storage-ktx'
    implementation 'com.google.firebase:firebase-auth-ktx'

    // Coroutines support for Firebase
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3'
}
*/

// 1. Firebase Data Models
data class Message @OptIn(ExperimentalFoundationApi::class) constructor(
    val id: String = "",
    val content: String = "",
    val senderId: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val mediaUrl: String? = null,
    val mediaType: MediaType? = null
)

// 2. Firebase Repository
//class FirebaseRepository {
//    private val auth = Firebase.auth
//    private val firestore = Firebase.firestore
//    private val storage = Firebase.storage.reference
//
//    private val messagesCollection = firestore.collection("messages")
//
//    // Message operations
//    @OptIn(ExperimentalFoundationApi::class)
//    suspend fun sendMessage(content: String, mediaUrl: String? = null, mediaType: MediaType? = null): Result<Message> =
//        withContext(Dispatchers.IO) {
//            try {
//                val message = Message(
//                    id = UUID.randomUUID().toString(),
//                    content = content,
//                    senderId = auth.currentUser?.uid ?: "",
//                    mediaUrl = mediaUrl,
//                    mediaType = mediaType
//                )
//
//                messagesCollection.document(message.id)
//                    .set(message)
//                    .await()
//
//                Result.success(message)
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//
//    // Upload media to Firebase Storage
//    @OptIn(ExperimentalFoundationApi::class)
//    suspend fun uploadMedia(uri: Uri, type: MediaType): Result<String> =
//        withContext(Dispatchers.IO) {
//            try {
//                val fileExtension = when (type) {
//                    is MediaType.Image -> "jpg"
//                    is MediaType.Video -> "mp4"
//                    is MediaType.Audio -> "mp3"
//                    else -> {}
//                }
//
//                val filename = "${UUID.randomUUID()}.$fileExtension"
//                val storageRef = storage.child("media/$filename")
//
//                val uploadTask = storageRef.putFile(uri)
//                val urlTask = uploadTask.continueWithTask { task ->
//                    if (!task.isSuccessful) {
//                        task.exception?.let { throw it }
//                    }
//                    storageRef.downloadUrl
//                }.await()
//
//                Result.success(urlTask.toString())
//            } catch (e: Exception) {
//                Result.failure(e)
//            }
//        }
//
//    // Get real-time messages updates
//    fun getMessagesFlow(): Flow<List<Message>> = callbackFlow {
//        val subscription = messagesCollection
//            .orderBy("timestamp", Query.Direction.DESCENDING)
//            .limit(100)
//            .addSnapshotListener { snapshot, error ->
//                if (error != null) {
//                    close(error)
//                    return@addSnapshotListener
//                }
//
//                val messages = snapshot?.documents?.mapNotNull { doc ->
//                    doc.toObject(Message::class.java)
//                } ?: emptyList()
//
//                trySend(messages)
//            }
//
//        awaitClose { subscription.remove() }
//    }
//}
//
//// 3. Enhanced ChatViewModel with Firebase
//class ChatViewModel(
//    private val firebaseRepository: FirebaseRepository,
//    private val mediaContentHandler: MediaContentHandler
//) : ViewModel() {
//    private val _uiState = MutableStateFlow(ChatUiState())
//    val uiState: StateFlow<ChatUiState> = _uiState
//
//    init {
//        // Collect messages updates
//        viewModelScope.launch {
//            firebaseRepository.getMessagesFlow()
//                .catch { error ->
//                    _uiState.update { it.copy(errorMessage = error.message) }
//                }
//                .collect { messages ->
//                    _uiState.update { it.copy(messages = messages) }
//                }
//        }
//    }
//
//    @OptIn(ExperimentalFoundationApi::class)
//    fun sendMessage(text: String) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isSending = true) }
//
//            firebaseRepository.sendMessage(content = text)
//                .onSuccess {
//                    _uiState.update { state ->
//                        state.copy(
//                            isSending = false,
//                            currentText = "",
//                            errorMessage = null
//                        )
//                    }
//                }
//                .onFailure { error ->
//                    _uiState.update { state ->
//                        state.copy(
//                            isSending = false,
//                            errorMessage = error.message
//                        )
//                    }
//                }
//        }
//    }
//
//    @OptIn(ExperimentalFoundationApi::class)
//    fun uploadAndSendMedia(uri: Uri, type: MediaType) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isUploading = true) }
//
//            firebaseRepository.uploadMedia(uri, type)
//                .onSuccess { mediaUrl ->
//                    firebaseRepository.sendMessage(
//                        content = "",
//                        mediaUrl = mediaUrl,
//                        mediaType = type
//                    ).onSuccess {
//                        _uiState.update { state ->
//                            state.copy(
//                                isUploading = false,
//                                errorMessage = null
//                            )
//                        }
//                    }
//                }
//                .onFailure { error ->
//                    _uiState.update { state ->
//                        state.copy(
//                            isUploading = false,
//                            errorMessage = error.message
//                        )
//                    }
//                }
//        }
//    }
//}

// 4. Updated ChatUiState
data class ChatUiState(
    val isBottomRowExpanded: Boolean = false,
    val isTextListVisible: Boolean = false,
    val isUploading: Boolean = false,
    val isSending: Boolean = false,
    val currentText: String = "",
    val messages: List<Message> = emptyList(),
    val errorMessage: String? = null
)

// 5. Message Display Component
//@Composable
//fun MessageList(
//    messages: List<Message>,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(
//        modifier = modifier,
//        reverseLayout = true
//    ) {
//        items(messages) { message ->
//            MessageItem(message = message)
//        }
//    }
//}

//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//fun MessageItem(
//    message: Message,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier
//            .padding(8.dp)
//            .fillMaxWidth()
//    ) {
//        // Message content
//        if (message.content.isNotEmpty()) {
//            Text(
//                text = message.content,
//                style = MaterialTheme.typography.bodyLarge
//            )
//        }
//
//        // Media content if present
//        message.mediaUrl?.let { url ->
//            when (message.mediaType) {
//                is MediaType.Image -> {
//                    AsyncImage(
//                        model = url,
//                        contentDescription = "Image",
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .aspectRatio(16f/9f)
//                            .clip(RoundedCornerShape(8.dp)),
//                        contentScale = ContentScale.Crop
//                    )
//                }
//                is MediaType.Video -> {
//                    // Video preview with play button
//                    Box(
//                        modifier = Modifier
//                            .fillMaxWidth()
//                            .aspectRatio(16f/9f)
//                            .clip(RoundedCornerShape(8.dp))
//                            .background(MaterialTheme.colorScheme.surface)
//                    ) {
//                        AsyncImage(
//                            model = url,
//                            contentDescription = "Video thumbnail",
//                            modifier = Modifier.fillMaxSize(),
//                            contentScale = ContentScale.Crop
//                        )
//                        Icon(
//                            imageVector = Icons.Default.PlayCircle,
//                            contentDescription = "Play",
//                            modifier = Modifier
//                                .size(48.dp)
//                                .align(Alignment.Center)
//                        )
//                    }
//                }
//                is MediaType.Audio -> {
//                    AudioPlayer(
//                        uri = Uri.parse(url),
//                        title = "Audio message",
//                        onPlayStateChanged = { }
//                    )
//                }
//                else -> Unit
//            }
//        }
//
//        // Timestamp
//        Text(
//            text = SimpleDateFormat("MMM dd, HH:mm", Locale.getDefault())
//                .format(Date(message.timestamp)),
//            style = MaterialTheme.typography.bodySmall,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//    }
//}


// Data class for user profile
data class UserProfile(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val photoUrl: String? = null,
    val bio: String = "",
    val location: String = "",
    val joinDate: Long = System.currentTimeMillis(),
    val stats: UserStats = UserStats(),
    val preferences: UserPreferences = UserPreferences()
)

data class UserStats(
    val postsCount: Int = 0,
    val followersCount: Int = 0,
    val followingCount: Int = 0,
    val totalLikes: Int = 0
)

data class UserPreferences(
    val isDarkMode: Boolean = false,
    val notificationsEnabled: Boolean = true,
    val privacyLevel: PrivacyLevel = PrivacyLevel.PUBLIC,
    val language: String = "English"
)

enum class PrivacyLevel {
    PUBLIC, PRIVATE, FRIENDS_ONLY
}

// ViewModel
//class ProfileViewModel(
//    private val firebaseRepository: FirebaseRepository
//) : ViewModel() {
//    private val _uiState = MutableStateFlow(ProfileUiState())
//    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()
//
//    init {
//        loadUserProfile()
//    }
//
//    private fun loadUserProfile() {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isLoading = true) }
//
//            firebaseRepository.getCurrentUserProfile()
//                .onSuccess { profile ->
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            userProfile = profile,
//                            error = null
//                        )
//                    }
//                }
//                .onFailure { error ->
//                    _uiState.update {
//                        it.copy(
//                            isLoading = false,
//                            error = error.message
//                        )
//                    }
//                }
//        }
//    }
//
//    fun updateProfile(updatedProfile: UserProfile) {
//        viewModelScope.launch {
//            _uiState.update { it.copy(isUpdating = true) }
//
//            firebaseRepository.updateUserProfile(updatedProfile)
//                .onSuccess {
//                    _uiState.update {
//                        it.copy(
//                            isUpdating = false,
//                            userProfile = updatedProfile,
//                            error = null
//                        )
//                    }
//                }
//                .onFailure { error ->
//                    _uiState.update {
//                        it.copy(
//                            isUpdating = false,
//                            error = error.message
//                        )
//                    }
//                }
//        }
//    }
//}

data class ProfileUiState(
    val isLoading: Boolean = false,
    val isUpdating: Boolean = false,
    val userProfile: UserProfile? = null,
    val error: String? = null
)

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ProfileScreen(
//    viewModel: ProfileViewModel,
//    onNavigateToSettings: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    val uiState by viewModel.uiState.collectAsState()
//    var showEditDialog by remember { mutableStateOf(false) }
//
//    Scaffold(
//        topBar = {
//            TopAppBar(
//                title = { Text("Profile") },
//                actions = {
//                    IconButton(onClick = onNavigateToSettings) {
//                        Icon(Icons.Default.Settings, "Settings")
//                    }
//                }
//            )
//        }
//    ) { padding ->
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(padding)
//        ) {
//            if (uiState.isLoading) {
//                LoadingIndicator()
//            } else {
//                uiState.userProfile?.let { profile ->
//                    ProfileContent(
//                        profile = profile,
//                        onEditClick = { showEditDialog = true }
//                    )
//                }
//            }
//
//            // Error snackbar
//            uiState.error?.let { error ->
//                Snackbar(
//                    modifier = Modifier
//                        .align(Alignment.BottomCenter)
//                        .padding(16.dp)
//                ) {
//                    Text(error)
//                }
//            }
//        }
//
//        if (showEditDialog) {
//            EditProfileDialog(
//                profile = uiState.userProfile!!,
//                onDismiss = { showEditDialog = false },
//                onSave = {
//                    viewModel.updateProfile(it)
//                    showEditDialog = false
//                }
//            )
//        }
//    }
//}

//@Composable
//fun ProfileContent(
//    profile: UserProfile,
//    onEditClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    LazyColumn(
//        modifier = modifier.fillMaxSize(),
//        contentPadding = PaddingValues(16.dp)
//    ) {
//        // Header Section
//        item {
//            ProfileHeader(
//                profile = profile,
//                onEditClick = onEditClick
//            )
//        }
//
//        // Stats Section
//        item {
//            Spacer(modifier = Modifier.height(24.dp))
//            StatsSection(stats = profile.stats)
//        }
//
//        // About Section
//        item {
//            Spacer(modifier = Modifier.height(24.dp))
//            AboutSection(profile = profile)
//        }
//
//        // Preferences Section
//        item {
//            Spacer(modifier = Modifier.height(24.dp))
//            PreferencesSection(preferences = profile.preferences)
//        }
//    }
//}

//@Composable
//fun ProfileHeader(
//    profile: UserProfile,
//    onEditClick: () -> Unit,
//    modifier: Modifier = Modifier
//) {
//    Column(
//        modifier = modifier.fillMaxWidth(),
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        Box(
//            modifier = Modifier.size(120.dp)
//        ) {
//            AsyncImage(
//                model = profile.photoUrl ?: "/api/placeholder/120/120",
//                contentDescription = "Profile photo",
//                modifier = Modifier
//                    .fillMaxSize()
//                    .clip(CircleShape)
//                    .border(2.dp, MaterialTheme.colorScheme.primary, CircleShape),
//                contentScale = ContentScale.Crop
//            )
//
//            IconButton(
//                onClick = onEditClick,
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .size(36.dp)
//                    .background(MaterialTheme.colorScheme.primary, CircleShape)
//            ) {
//                Icon(
//                    Icons.Default.Edit,
//                    contentDescription = "Edit profile",
//                    tint = MaterialTheme.colorScheme.onPrimary
//                )
//            }
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = profile.name,
//            style = MaterialTheme.typography.headlineMedium,
//            fontWeight = FontWeight.Bold
//        )
//
//        Text(
//            text = profile.email,
//            style = MaterialTheme.typography.bodyLarge,
//            color = MaterialTheme.colorScheme.onSurfaceVariant
//        )
//
//        if (profile.location.isNotEmpty()) {
//            Spacer(modifier = Modifier.height(8.dp))
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center
//            ) {
//                Icon(
//                    Icons.Default.LocationOn,
//                    contentDescription = null,
//                    modifier = Modifier.size(16.dp),
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//                Spacer(modifier = Modifier.width(4.dp))
//                Text(
//                    text = profile.location,
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}

@Composable
fun StatsSection(
    stats: UserStats,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            StatItem("Posts", stats.postsCount)
            VerticalDivider()
            StatItem("Followers", stats.followersCount)
            VerticalDivider()
            StatItem("Following", stats.followingCount)
            VerticalDivider()
            StatItem("Likes", stats.totalLikes)
        }
    }
}

@Composable
fun StatItem(
    label: String,
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count.toString(),
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

//@Composable
//fun AboutSection(
//    profile: UserProfile,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = "About",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = profile.bio.ifEmpty { "No bio added yet" },
//                style = MaterialTheme.typography.bodyLarge
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            Row(
//                verticalAlignment = Alignment.CenterVertically
//            ) {
//                Icon(
//                    Icons.Default.Call,
//                    contentDescription = null,
//                    modifier = Modifier.size(16.dp),
//                    tint = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(
//                    text = "Joined ${
//                        SimpleDateFormat("MMMM yyyy", Locale.getDefault())
//                            .format(Date(profile.joinDate))
//                    }",
//                    style = MaterialTheme.typography.bodyMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
//        }
//    }
//}

//@Composable
//fun PreferencesSection(
//    preferences: UserPreferences,
//    modifier: Modifier = Modifier
//) {
//    Card(
//        modifier = modifier.fillMaxWidth(),
//        shape = RoundedCornerShape(16.dp)
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp)
//        ) {
//            Text(
//                text = "Preferences",
//                style = MaterialTheme.typography.titleLarge,
//                fontWeight = FontWeight.Bold
//            )
//
//            Spacer(modifier = Modifier.height(16.dp))
//
//            PreferenceItem(
//                icon = Icons.Default.DarkMode,
//                title = "Dark Mode",
//                subtitle = if (preferences.isDarkMode) "Enabled" else "Disabled"
//            )
//
//            PreferenceItem(
//                icon = Icons.Default.Notifications,
//                title = "Notifications",
//                subtitle = if (preferences.notificationsEnabled) "Enabled" else "Disabled"
//            )
//
//            PreferenceItem(
//                icon = Icons.Default.Lock,
//                title = "Privacy",
//                subtitle = preferences.privacyLevel.name.replace("_", " ").capitalize()
//            )
//
//            PreferenceItem(
//                icon = Icons.Default.Language,
//                title = "Language",
//                subtitle = preferences.language
//            )
//        }
//    }
//}

//@Composable
//fun PreferenceItem(
//    icon: ImageVector,
//    title: String,
//    subtitle: String,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        modifier = modifier
//            .fillMaxWidth()
//            .padding(vertical = 8.dp),
//        verticalAlignment = Alignment.CenterVertically
//    ) {
//        Icon(
//            icon,
//            contentDescription = null,
//            tint = MaterialTheme.colorScheme.primary
//        )
//
//        Spacer(modifier = Modifier.width(16.dp))
//
//        Column {
//            Text(
//                text = title,
//                style = MaterialTheme.typography.bodyLarge
//            )
//            Text(
//                text = subtitle,
//                style = MaterialTheme.typography.bodyMedium,
//                color = MaterialTheme.colorScheme.onSurfaceVariant
//            )
//        }
//    }
//}

//@Composable
//fun EditProfileDialog(
//    profile: UserProfile,
//    onDismiss: () -> Unit,
//    onSave: (UserProfile) -> Unit
//) {
//    var name by remember { mutableStateOf(profile.name) }
//    var bio by remember { mutableStateOf(profile.bio) }
//    var location by remember { mutableStateOf(profile.location) }
//
//    Dialog(onDismissRequest = onDismiss) {
//        Card(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp),
//            shape = RoundedCornerShape(16.dp)
//        ) {
//            Column(
//                modifier = Modifier
//                    .padding(16.dp)
//                    .fillMaxWidth()
//            ) {
//                Text(
//                    text = "Edit Profile",
//                    style = MaterialTheme.typography.titleLarge,
//                    fontWeight = FontWeight.Bold
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                OutlinedTextField(
//                    value = name,
//                    onValueChange = { name = it },
//                    label = { Text("Name") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                OutlinedTextField(
//                    value = bio,
//                    onValueChange = { bio = it },
//                    label = { Text("Bio") },
//                    modifier = Modifier.fillMaxWidth(),
//                    minLines = 3
//                )
//
//                Spacer(modifier = Modifier.height(8.dp))
//
//                OutlinedTextField(
//                    value = location,
//                    onValueChange = { location = it },
//                    label = { Text("Location") },
//                    modifier = Modifier.fillMaxWidth()
//                )
//
//                Spacer(modifier = Modifier.height(16.dp))
//
//                Row(
//                    modifier = Modifier.fillMaxWidth(),
//                    horizontalArrangement = Arrangement.End
//                ) {
//                    TextButton(onClick = onDismiss) {
//                        Text("Cancel")
//                    }
//
//                    Spacer(modifier = Modifier.width(8.dp))
//
//                    Button(
//                        onClick = {
//                            onSave(profile.copy(
//                                name = name,
//                                bio = bio,
//                                location = location
//                            ))
//                        }
//                    ) {
//                        Text("Save")
//                    }
//                }
//            }
//        }
//    }
//}

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}