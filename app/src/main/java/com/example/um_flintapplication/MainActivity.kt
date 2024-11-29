package com.example.um_flintapplication
import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.example.um_flintapplication.apiRequests.AnnouncementItem
import com.example.um_flintapplication.apiRequests.EventItem
import com.example.um_flintapplication.apiRequests.NewsItem
import com.example.um_flintapplication.apiRequests.Retrofit
import com.example.um_flintapplication.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.skydoves.sandwich.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var toggle: ActionBarDrawerToggle
    private lateinit var googleSignIn : Auth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)
        binding.appBarMain.toolbar.title = "University of Michigan - Flint" // Set the toolbar title here

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)

        // Initialize AppBarConfiguration with top-level destinations
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_resources_departments, R.id.nav_resources_maps, R.id.nav_announcements, R.id.nav_scheduling_reserve_room, R.id.nav_scheduling_schedule_advisor, R.id.nav_scheduling_group_meetings, R.id.nav_messaging_discord
            ), drawerLayout
        )

        // Set up ActionBar with NavController and AppBarConfiguration
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Initialize ActionBarDrawerToggle
        toggle = ActionBarDrawerToggle(
            this, drawerLayout, binding.appBarMain.toolbar,
            R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        // Add DrawerListener to DrawerLayout and sync state
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView = findViewById<NavigationView>(R.id.nav_view)
        navigationView.setNavigationItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.nav_home -> {
                    // Navigate to the Home page
                    val intent = Intent(this, MainActivity::class.java) // Replace with your Home activity
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_academic_calendar -> {
                    // Navigate to Academic Calendar page
                    val intent = Intent(this, AcademicCalendar::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_departments -> {
                    // Navigate to Departments page
                    val intent = Intent(this, DepartmentInformationActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_resources_maps -> {
                    // Navigate to Maps page
                    val intent = Intent(this, MapsPage::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_scheduling_reserve_room -> {
                    // Navigate to Reserve Room page
                    val intent = Intent(this, ScheduleGroupMeetingActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_scheduling_schedule_advisor -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, ScheduleAdvisorActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_announcements -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, AlertsActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_send_announcements -> {
                    // Navigate to Announcements page
                    val intent = Intent(this, SendAnnouncementActivity::class.java)
                    startActivity(intent)
                    true
                }
                R.id.nav_messaging_discord-> {
                    val url = "https://discord.gg/AEefzfqSB9"
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)
                    startActivity(intent)
                    true
                }
                R.id.nav_messaging_student_messaging-> {
                    // Navigate to Announcements page
                    val intent = Intent(this, MessagingActivity::class.java)
                    startActivity(intent)
                    true
                }
                else -> false
            }
        }

        //Begin News
        CoroutineScope(Dispatchers.IO).launch {
            var news: List<NewsItem>? = null

            Retrofit(this@MainActivity).api.getNews(3).onSuccess {
                news = data
            }

            val layout = findViewById<LinearLayout>(R.id.NewsSection)

            news?.forEach { item ->
                val textView = TextView(this@MainActivity)

                textView.text = item.title
                textView.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                textView.setPadding(0, 8, 0, 0)

                val layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                    LinearLayout.LayoutParams.WRAP_CONTENT  // Height
                )
                textView.layoutParams = layoutParams

                withContext(Dispatchers.Main){
                    layout.addView(textView)
                }
            }
        }

        //Begin Alerts
        CoroutineScope(Dispatchers.IO).launch {
            var announcements: List<AnnouncementItem>? = null

            Retrofit(this@MainActivity).api.getAnnouncements(1).onSuccess {
                announcements = data
            }

            withContext(Dispatchers.Main) {
                val layout = findViewById<LinearLayout>(R.id.AlertSection)

                announcements?.forEach { item ->
                    val alertHeader = TextView(this@MainActivity)
                    alertHeader.text = item.title
                    alertHeader.setTypeface(null, Typeface.BOLD)
                    alertHeader.textSize = 16f
                    alertHeader.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.black))

                    val linearLayout = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                        LinearLayout.LayoutParams.WRAP_CONTENT  // Height
                    )
                    alertHeader.layoutParams = linearLayout
                    layout.addView(alertHeader)

                    val alertBody = TextView(this@MainActivity)
                    alertBody.text = Html.fromHtml(item.description.substring(0, 150) + "...", Html.FROM_HTML_MODE_LEGACY).trim()
                    alertBody.layoutParams = linearLayout
                    alertBody.setOnClickListener{openAlertsPage(alertBody)}

                    layout.addView(alertBody)
                }
            }
        }

//        //Begin events (NO IMAGE) !! TEMPORARY !!
//        CoroutineScope(Dispatchers.IO).launch{
//            val events = Retrofit(this@MainActivity).api.getEvents(3)
//
//            withContext(Dispatchers.Main){
//                val layout = findViewById<LinearLayout>(R.id.EventsSection)
//
//                events.forEach{ item ->
//                    val textview = TextView(this@MainActivity)
//
//                    textview.text = item.title
//                    textview.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
//                    textview.setPadding(0, 8, 0, 0)
//
//                    val layoutParams = LinearLayout.LayoutParams(
//                            LinearLayout.LayoutParams.WRAP_CONTENT, // Width
//                            LinearLayout.LayoutParams.WRAP_CONTENT  // Height
//                    )
//                    textview.layoutParams = layoutParams
//
//                    layout.addView(textview)
//                }
//            }
//        }

//        Begin events (WITH IMAGE) (and titles now too)
        CoroutineScope(Dispatchers.IO).launch {
            var events: List<EventItem>? = null

            Retrofit(this@MainActivity).api.getEvents(3).onSuccess {
                events = data
            }

            val finalEvents = events
            if(finalEvents!=null){
                val event1url = finalEvents[0].photo
                val event2url = finalEvents[1].photo
                val event3url = finalEvents[2].photo

                val eventimg1 = findViewById<ImageView>(R.id.event1)
                val eventimg2 = findViewById<ImageView>(R.id.event2)
                val eventimg3 = findViewById<ImageView>(R.id.event3)

                val layout = findViewById<LinearLayout>(R.id.EventTitles)

                withContext(Dispatchers.Main){
                    Glide.with(this@MainActivity)
                        .load(event1url)
                        .into(eventimg1)

                    Glide.with(this@MainActivity)
                        .load(event2url)
                        .into(eventimg2)

                    Glide.with(this@MainActivity)
                        .load(event3url)
                        .into(eventimg3)

                    finalEvents.forEach{item ->
                        val textview = TextView(this@MainActivity)

                        textview.text = item.title
                        textview.setTextColor(ContextCompat.getColor(this@MainActivity, R.color.white))
                        textview.setPadding(0, 8, 0, 0)
                        textview.width = layout.measuredWidth / 3
                        textview.maxLines = 3
                        textview.ellipsize = TextUtils.TruncateAt.END


                        val layoutParams = LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.WRAP_CONTENT, // Width
                            LinearLayout.LayoutParams.WRAP_CONTENT  // Height
                        )
                        layoutParams.leftMargin = 4

                        textview.layoutParams = layoutParams


                        layout.addView(textview)
                    }
                }
            }
        }

        // Basically to sign in you have to create an instance of the Auth class, making sure to
        // pass the activity to it (via 'this').

        // You can then call the silentLogin function, which has a callback that will give you a
        // GoogleSignInAccount where you can get email, names, profile picture, token, and id.
        //
        // Call the login function to force a sign in.
        // If the user has already logged in, then it just logs in automatically.
        // Else it will try to login the user.

        // Retrofit has an interceptor that will do this all for you, automatically adding a token.
        // However it uses the silent option, so make sure the user is logged in first.
        googleSignIn = Auth(this)

        // need to create a launcher if you are using login() in onCreate directly
        googleSignIn.createLauncher()

        var signInButton = findViewById<LinearLayout>(R.id.SignIn)
        signInButton.setOnClickListener{
            googleSignIn.login()
        }

        googleSignIn.silentLogin { cred ->
            if(cred!=null){
                Toast.makeText(this, "Logged in silently as "+cred.email, Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    // Override onOptionsItemSelected to toggle the drawer when the hamburger icon is clicked
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openAlertsPage(view: View) {
        val intent = Intent(this, AlertsActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Sign In page
    fun openSignInPage(view: View) {
        val intent = Intent(this, SignInActivity::class.java)
        startActivity(intent)
    }

    // Function to open the Events page
    fun openEventsPage(view: View) {
        val url = "https://events.umflint.edu/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    // Function to open the News page
    fun openNewsPage(view: View) {
        val url = "https://news.umflint.edu/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

    // Function to open the Maps page
    fun openMapsPage(view: View) {
        val intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
