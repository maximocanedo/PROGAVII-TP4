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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.paddingFromBaseline
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import frgp.utn.edu.ar.tp4.data.daoImpl.CategoryDaoImpl
import frgp.utn.edu.ar.tp4.ui.theme.TP4Theme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    val viewModel: MainViewModel by viewModels()
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
                        viewModel = viewModel
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldView(modifier: Modifier = Modifier, viewModel: MainViewModel) {

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
                    1 -> ModificarTabContent(viewModel)
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
    val categories = arrayOf("Verduras", "Frutas", "Almacen", "Lacteos", "Fiambres")
    var selectedText by remember { mutableStateOf(categories[0]) }
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        OutlinedTextField(
            value = viewModel.create__idText,
            placeholder = { Text(text = "ID") },
            onValueChange = {viewModel.onCreate__idTextChange(it)},
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
            value = viewModel.create__stockText.toString(),
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
                value = selectedText,
                onValueChange = {},
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                modifier = Modifier.menuAnchor()
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { item ->
                    DropdownMenuItem(
                        text = { Text(text = item) },
                        onClick = {
                            selectedText = item
                            expanded = false
                            Toast.makeText(context, item, Toast.LENGTH_SHORT).show()
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.padding(5.dp))
        Button(
            onClick = {
                /* TODO */
            }
        ) {
            Text(text = "Crear")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModificarTabContent(viewModel: MainViewModel) {
    val context = LocalContext.current
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember { mutableStateOf("") }

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
                    onValueChange = { viewModel.onCreate__idTextChange(it) },
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
                            val articleDaoImpl = ArticleDaoImpl()

                            articleDaoImpl.getArticleById(id.toInt())
                            println(articleDaoImpl)
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
            value = viewModel.create__nameText,
            onValueChange = {viewModel.onCreate__nameTextChange(it)},
            supportingText = { Text(text = viewModel.create__nameMsg) },
            isError = viewModel.create__nameError
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            placeholder = { Text(text = "Stock disponible") },
            value = "",
            onValueChange = { },
            supportingText = { Text(text = viewModel.create__stockMsg) },
            isError = viewModel.create__stockError
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
                value = selectedText,
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

@Composable
fun ListarTabContent(viewModel: MainViewModel) {
    var items by remember { mutableStateOf(listOf<String>()) }
    items = listOf("Producto 1", "Producto 2", "Producto 3", "Producto 4", "Producto 5")
    val categoryDaoImpl = CategoryDaoImpl()
    //items = categoryDaoImpl.getAllCategories().map { it.toString() }

    LazyColumn (
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        items.forEach() { item ->
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(100.dp),
                    shape = MaterialTheme.shapes.large,
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Row (
                            modifier = Modifier.fillMaxWidth().padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        )
                        {
                            Column {
                                Text(
                                    text = item,
                                    textAlign = TextAlign.Center,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            VerticalDivider(
                                modifier = Modifier.padding(8.dp)
                            )
                            Column {
                                Button(
                                    onClick = {
                                        /* TODO */
                                    },
                                    shape = MaterialTheme.shapes.small
                                ) {
                                    Icon(Icons.Default.Delete, contentDescription = "Delete")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    TP4Theme {
        ScaffoldView(modifier = Modifier, viewModel = MainViewModel())
    }
}