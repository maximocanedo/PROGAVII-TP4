package frgp.utn.edu.ar.tp4

import ArticleDaoImpl
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import frgp.utn.edu.ar.tp4.activity.Article.ArticleViewModel
import frgp.utn.edu.ar.tp4.data.daoImpl.CategoryDaoImpl
import frgp.utn.edu.ar.tp4.data.models.Article
import frgp.utn.edu.ar.tp4.ui.theme.TP4Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()
    val articleViewModel: ArticleViewModel by viewModels()
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TP4Theme {
                Scaffold (
                    topBar = {
                        TopAppBar(
                            title = {
                                Text(text = "TP4")
                            }
                        )
                    },
                ) { innerPadding ->
                    ScaffoldView(
                        modifier = Modifier.padding(innerPadding),
                        viewModel = viewModel,
                        articleViewModel = articleViewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldView(modifier: Modifier = Modifier, viewModel: MainViewModel, articleViewModel: ArticleViewModel) {

    val state = viewModel.selectedTabIndex
    val titles = listOf("Crear", "Modificar", "Listar")

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            PrimaryTabRow(selectedTabIndex = state) {
                titles.forEachIndexed { index, title ->
                    Tab(
                        selected = state == index,
                        onClick = { viewModel.onTabSelected(index) },
                        text = { Text(text = title, maxLines = 2, overflow = TextOverflow.Ellipsis) }
                    )
                }
            }
        }
    ) { it ->
        Column(modifier = Modifier.padding(it)) {
            Box(
                modifier = Modifier
                    .paddingFromBaseline(32.dp)
                    .fillMaxSize(),
                contentAlignment = Alignment.TopCenter
            ) {
                when (state) {
                    0 -> CrearTabContent(viewModel)
                    1 -> ModificarTabContent(viewModel, articleViewModel)
                    2 -> ListarTabContent(viewModel)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearTabContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    val categoryDaoImpl = CategoryDaoImpl()
    val articleDaoImpl = ArticleDaoImpl()
    LaunchedEffect (Unit) {
        withContext(Dispatchers.IO) {
            viewModel.onCreate__categoriesChange(categoryDaoImpl.getAllCategories())
            viewModel.onCreate__selectedCategoryIndexChange(0)
            viewModel.onCreate__selectedCategoryTextChange(viewModel.create__categories[0].getDescription())
        }
    }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = viewModel.create__idText,
            placeholder = { Text(text = "ID") },
            onValueChange = {
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.onCreate__idTextChange(it)
                }
            },
            supportingText = { Text(text = viewModel.create__idMsg) },
            isError = viewModel.create__idError
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            placeholder = { Text(text = "Nombre") },
            value = viewModel.create__nameText,
            onValueChange = {viewModel.onCreate__nameTextChange(it)},
            supportingText = { Text(text = viewModel.create__nameMsg) },
            isError = viewModel.create__nameError
        )
        Spacer(modifier = Modifier.padding(5.dp))
        OutlinedTextField(
            placeholder = { Text(text = "Stock disponible") },
            value = viewModel.create__stockText,
            onValueChange = {viewModel.onCreate__stockTextChange(it.toInt().toString())},
            supportingText = { Text(text = viewModel.create__stockMsg) },
            isError = viewModel.create__stockError
        )
        Spacer(modifier = Modifier.padding(5.dp))
        ExposedDropdownMenuBox (
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                value = viewModel.create__selectedCategoryText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                viewModel.create__categories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item.getDescription()) },
                        onClick = {
                            viewModel.onCreate__selectedCategoryTextChange(item.getDescription())
                            viewModel.onCreate__selectedCategoryIndexChange(viewModel.create__categories.indexOf(item))
                            expanded = false
                            Toast.makeText(context, item.getDescription(), Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Button(
            onClick = {
                CoroutineScope(Dispatchers.IO).launch {
                    val article = Article(
                        viewModel.create__idText.toInt(),
                        viewModel.create__nameText,
                        viewModel.create__stockText.toInt(),
                        viewModel.create__categories[viewModel.selectedCategoryIndex]
                    )
                    articleDaoImpl.insertArticle(article)
                    viewModel.onCreate__idTextChange("")
                }
                Toast.makeText(context, "Articulo creado", Toast.LENGTH_SHORT).show()
                viewModel.onCreate__nameTextChange("")
                viewModel.onCreate__stockTextChange("")
                viewModel.onCreate__selectedCategoryIndexChange(0)
            }
        ) {
            Text(text = "Crear")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarTabContent(viewModel: MainViewModel, articleViewModel: ArticleViewModel) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }
    val articleDaoImpl = ArticleDaoImpl()

    Column(
        modifier = Modifier
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .background(Color.LightGray)
                .padding(16.dp)
        ) {
            Column {
                OutlinedTextField(
                    value = viewModel.create__idText,
                    placeholder = { Text(text = "ID") },
                    onValueChange = {
                        CoroutineScope(Dispatchers.IO).launch {
                            viewModel.onCreate__idTextChange(it)
                        }
                    },
                    supportingText = { Text(text = viewModel.create__idMsg) },
                    isError = viewModel.create__idError,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.White)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Button(
                    onClick = {
                        val id = viewModel.create__idText
                        CoroutineScope(Dispatchers.IO).launch {
                            val article = articleDaoImpl.getArticleById(id.toInt())
                            withContext(Dispatchers.Main) {
                                articleViewModel.updateFieldsFromArticle(article)
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor  = Color.DarkGray,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.align(Alignment.End)
                ) {
                    Text(text = "Buscar")
                }
            }
        }

        Spacer(modifier = Modifier.height(96.dp))
        OutlinedTextField(
            placeholder = { Text(text = "Nombre") },
            value = articleViewModel.create__nameText,
            onValueChange = {articleViewModel.create__nameText = it},
            supportingText = { Text(text = articleViewModel.create__nameMsg) },
            isError = articleViewModel.create__nameError
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            placeholder = { Text(text = "Stock disponible") },
            value = articleViewModel.create__stockText,
            onValueChange = { articleViewModel.create__stockText = it },
            supportingText = { Text(text = articleViewModel.create__stockMsg) },
            isError = articleViewModel.create__stockError
        )
        Spacer(modifier = Modifier.height(16.dp))
        ExposedDropdownMenuBox (
            expanded = expanded,
            onExpandedChange = {
                expanded = !expanded
            }
        ) {
            OutlinedTextField(
                placeholder = { Text(text = "Categoria") },
                value = articleViewModel.create__categoryText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                DropdownMenuItem(
                    text = { Text(text = "") },
                    onClick = {
                        selectedText = ""
                        expanded = false
                        Toast.makeText(context, "", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
        Spacer(modifier = Modifier.height(64.dp))
        Button(
            onClick = {
                /* TODO */
            },
            colors = ButtonDefaults.buttonColors(
                containerColor  = Color.DarkGray,
                contentColor = Color.White
            ),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Modificar")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListarTabContent(viewModel: MainViewModel) {
    val items = viewModel.items
    var isRefreshing by remember { mutableStateOf(false) }

    Column {
        TextButton(
            modifier = Modifier
                .paddingFromBaseline(top = 16.dp, bottom = 16.dp),
            onClick = {
                isRefreshing = true
                CoroutineScope(Dispatchers.IO).launch {
                    viewModel.loadItems()
                    isRefreshing = false
                }
            }
        ) {
            Text(text = if (isRefreshing) "Cargando..." else "Actualizar")
        }
        if (viewModel.listError) {
            Text(text = viewModel.emptyListMessage, color = Color.Red)
        } else if (items.isEmpty()) {
            Text(text = viewModel.emptyListMessage)
        } else {
            LazyColumn {
                items(items) {
                    ListItem(
                        headlineContent = { Text(it.getName()) },
                        supportingContent = { Text(it.getCategory().getDescription()) },
                        trailingContent = { Text(it.getStock().toString()) }
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TP4Theme {
        ScaffoldView(modifier = Modifier, viewModel = MainViewModel(), articleViewModel = ArticleViewModel())
    }
}