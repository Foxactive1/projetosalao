package dev.codepolix.salongeneric

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val Navy = Color(0xFF101B35)
private val Blue = Color(0xFF2563EB)
private val Surface = Color(0xFFF4F7FC)
private val Ink = Color(0xFF17233A)

private data class Item(val name: String, val detail: String, val price: String)
private val products = listOf(
    Item("Kit Hidratação", "Tratamento capilar", "R$ 75,90"),
    Item("Shampoo Profissional", "Cuidados diários", "R$ 49,90"),
    Item("Máscara Nutritiva", "Tratamento intensivo", "R$ 64,90")
)
private val services = listOf(
    Item("Corte e finalização", "Duração estimada: 60 min", "R$ 80,00"),
    Item("Escova", "Duração estimada: 45 min", "R$ 55,00"),
    Item("Hidratação", "Duração estimada: 50 min", "R$ 70,00")
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { InnovaSalonApp() }
    }
}

@Composable
private fun InnovaSalonApp() {
    var selected by remember { mutableIntStateOf(0) }
    var dark by remember { mutableStateOf(false) }
    val bg = if (dark) Color(0xFF0C1425) else Surface
    val foreground = if (dark) Color.White else Ink
    MaterialTheme(colorScheme = if (dark) darkColorScheme(primary = Color(0xFF83B2FF)) else lightColorScheme(primary = Blue)) {
        Scaffold(
            containerColor = bg,
            topBar = {
                Surface(color = Navy) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 18.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(Modifier.weight(1f)) {
                            Text("InNovaIdeia®", color = Color.White, fontSize = 22.sp, fontWeight = FontWeight.Bold)
                            Text("SALON  •  Gestão & Beleza", color = Color(0xFFBFD2F9), fontSize = 12.sp)
                        }
                        IconButton(onClick = { dark = !dark }) {
                            Icon(if (dark) Icons.Default.LightMode else Icons.Default.DarkMode, "Alternar tema", tint = Color.White)
                        }
                    }
                }
            },
            bottomBar = {
                NavigationBar(containerColor = if (dark) Navy else Color.White) {
                    listOf(Triple("Início", Icons.Default.Home, 0), Triple("Produtos", Icons.Default.ShoppingBag, 1), Triple("Serviços", Icons.Default.Event, 2), Triple("Perfil", Icons.Default.Person, 3)).forEach { (label, icon, index) ->
                        NavigationBarItem(
                            selected = selected == index,
                            onClick = { selected = index },
                            icon = { Icon(icon, contentDescription = label) },
                            label = { Text(label) }
                        )
                    }
                }
            }
        ) { padding ->
            LazyColumn(
                Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        when (selected) { 0 -> "Bem-vindo ao salão"; 1 -> "Nossos produtos"; 2 -> "Serviços disponíveis"; else -> "Meu perfil" },
                        color = foreground, fontSize = 25.sp, fontWeight = FontWeight.Bold
                    )
                }
                when (selected) {
                    0 -> {
                        item { InfoCard("Seu espaço de beleza", "Conheça nossos serviços e produtos em uma experiência simples e moderna.", dark) }
                        item { Text("Destaques", color = foreground, fontSize = 19.sp, fontWeight = FontWeight.SemiBold) }
                        items(services.take(2)) { ProductCard(it, dark) }
                        item { Button(onClick = { selected = 2 }, modifier = Modifier.fillMaxWidth()) { Text("Explorar serviços") } }
                    }
                    1 -> items(products) { ProductCard(it, dark) }
                    2 -> items(services) { ProductCard(it, dark) }
                    else -> {
                        item { InfoCard("Área do cliente", "Em breve: autenticação, histórico e agendamentos integrados à API.", dark) }
                        item { InfoCard("InNovaIdeia®", "Tecnologia e inovação para transformar negócios.", dark) }
                    }
                }
                item { Text("© InNovaIdeia® • Interface demonstrativa", color = if (dark) Color.LightGray else Color.Gray, fontSize = 11.sp) }
            }
        }
    }
}

@Composable
private fun InfoCard(title: String, description: String, dark: Boolean) {
    Card(colors = CardDefaults.cardColors(containerColor = if (dark) Navy else Color.White), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, fontSize = 19.sp, fontWeight = FontWeight.Bold)
            Text(description, fontSize = 14.sp)
        }
    }
}

@Composable
private fun ProductCard(item: Item, dark: Boolean) {
    Card(colors = CardDefaults.cardColors(containerColor = if (dark) Navy else Color.White), shape = RoundedCornerShape(18.dp), modifier = Modifier.fillMaxWidth()) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Spa, contentDescription = null, tint = if (dark) Color(0xFF83B2FF) else Blue, modifier = Modifier.size(36.dp))
            Spacer(Modifier.width(14.dp))
            Column(Modifier.weight(1f)) {
                Text(item.name, fontWeight = FontWeight.Bold)
                Text(item.detail, fontSize = 12.sp)
                Spacer(Modifier.height(6.dp))
                Text(item.price, color = if (dark) Color(0xFF83B2FF) else Blue, fontWeight = FontWeight.Bold)
            }
        }
    }
}
