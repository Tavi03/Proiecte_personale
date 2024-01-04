import random
import socket

# Functie pentru alegerea aleatoare a labirintului
def pick_maze():
    random_number = random.randint(1,5)
    maze_files = [
        # paths to the 5 mazes to choose randomly
        "C:\\Users\Tavi\Desktop\\temavs\\an 2 sem 1\\retele de calculatoare\\tema2\\maze1.txt",
        "C:\\Users\Tavi\Desktop\\temavs\\an 2 sem 1\\retele de calculatoare\\tema2\\maze2.txt",
        "C:\\Users\Tavi\Desktop\\temavs\\an 2 sem 1\\retele de calculatoare\\tema2\\maze3.txt",
        "C:\\Users\Tavi\Desktop\\temavs\\an 2 sem 1\\retele de calculatoare\\tema2\\maze4.txt",
        "C:\\Users\Tavi\Desktop\\temavs\\an 2 sem 1\\retele de calculatoare\\tema2\\maze5.txt"
    ]
    selected_maze_file = maze_files[random_number - 1]
    
    with open(selected_maze_file, "r") as file:
        return [list(line.strip()) for line in file]
 
# Functie pentru a vedea daca monstrul blocheaza calea jucatorului folosind
# algoritmul DFS
def monster_in_the_way(maze, row, col):
    visited = [[0 for item in range(10)] for item in range(10)]
    finish_pos = get_finish_pos(maze)
    nodes = [0 for i in range(64)]
    nodes[0] = player_pos
    noNodes = 1
    found = False
    while found != True and noNodes > 0:
        current = nodes.pop(0)
        noNodes -= 1

        if current == finish_pos:
            found = True
            # print("am gasit iesirea")
        else:
            possible_moves = [[0, -1], [-1, 0], [0, 1], [1, 0]]
            for i in range (4):
                nextMove = [current[0] + possible_moves[i][0], current[1] + possible_moves[i][1]]
                # print("nextMove =", nextMove)
                # Daca mutarea este valida
                if maze[nextMove[0]][nextMove[1]] != '#' and visited[nextMove[0]][nextMove[1]] != 1:
                    if nextMove == [row, col]: # daca am gasit monstrul
                        # print("am dat de monstru")
                        return 1
                    elif nextMove == finish_pos:
                        return 0
                    nodes.insert(0, nextMove)
                    noNodes += 1
                    visited[nextMove[0]][nextMove[1]] = 1

    # print("n am gasit iesirea")
    return 1

# Functie pentru gasirea iesirii
def get_finish_pos(maze):
    for i in range (10):
        if maze[0][i] == ' ':
            return [0, i]
        if maze[9][i] == ' ':
            return [9, i]
        if maze[i][0] == ' ':
            return [i, 0]
        if maze[i][9] == ' ':
            return [i, 9]

# Functie pentru generarea jucatorului
def pick_player_position(maze):
    row = random.randint(1, 8)
    col = random.randint(1, 8)
    finish_pos = get_finish_pos(maze)
    while abs(finish_pos[0] - row) < 3 or abs(finish_pos[1] - col) < 3:
        row = random.randint(1, 8)
        col = random.randint(1, 8)
    return [row, col]

# Functie pentru generarea monstrului
def pick_monster_position(maze):
    row = random.randint(1, 8)
    col = random.randint(1, 8)
    # monster_in_the_way(maze, row, col)
    while abs(player_pos[0] - row) < 4 or abs(player_pos[1] - col) < 4 or monster_in_the_way(maze, row, col):
        row = random.randint(1, 8)
        col = random.randint(1, 8)
    return [row, col]

# Functie pentru a efectua mutarea jucatorului
def do_player_move(maze, move, player_pos):
    # print("player_pos = ", player_pos)
    if move == 'U':
        if maze[player_pos[0] - 1][player_pos[1]] == '#':
            return -1, player_pos
        elif maze[player_pos[0] - 1][player_pos[1]] == 'M':
            return -2, player_pos
        elif [player_pos[0] - 1, player_pos[1]] == finish_pos:
            return 1, player_pos
        else:
            maze[player_pos[0]][player_pos[1]] = ' '
            player_pos = [player_pos[0] - 1, player_pos[1]]
            maze[player_pos[0]][player_pos[1]] = 'J'
            return 0, player_pos
        
    if move == 'D':
        if maze[player_pos[0] + 1][player_pos[1]] == '#':
            return -1, player_pos
        elif maze[player_pos[0] + 1][player_pos[1]] == 'M':
            return -2, player_pos
        elif [player_pos[0] + 1, player_pos[1]] == finish_pos:
            return 1, player_pos
        else:
            maze[player_pos[0]][player_pos[1]] = ' '
            player_pos = [player_pos[0] + 1, player_pos[1]]
            maze[player_pos[0]][player_pos[1]] = 'J'
            return 0, player_pos
        
    if move == 'L':
        if maze[player_pos[0]][player_pos[1] - 1] == '#':
            return -1, player_pos
        elif maze[player_pos[0]][player_pos[1] - 1] == 'M':
            return -2, player_pos
        elif [player_pos[0], player_pos[1] - 1] == finish_pos:
            return 1, player_pos
        else:
            maze[player_pos[0]][player_pos[1]] = ' '
            player_pos = [player_pos[0], player_pos[1] - 1]
            maze[player_pos[0]][player_pos[1]] = 'J'
            return 0, player_pos
        
    if move == 'R':
        if maze[player_pos[0]][player_pos[1] + 1] == '#':
            return -1, player_pos
        elif maze[player_pos[0]][player_pos[1] + 1] == 'M':
            return -2, player_pos
        elif [player_pos[0], player_pos[1] + 1] == finish_pos:
            return 1, player_pos
        else:
            maze[player_pos[0]][player_pos[1]] = ' '
            player_pos = [player_pos[0], player_pos[1] + 1]
            maze[player_pos[0]][player_pos[1]] = 'J'
            return 0, player_pos
    
def print_maze(maze):
    for row in maze:
        print(" ".join(map(str, row)))
    print("\n")

# Stabilirea legaturii intre server si client
serverAddr = '127.0.0.1'
serverPort = 30000
serverSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
serverSocket.bind(('', serverPort))
print('Serverul este functional si asteapta...')

while True:
    message, clientAddr = serverSocket.recvfrom(300)
    print('Am primit de la client mesajul:', message.decode())

    # Incepe jocul
    while message.decode() == "START":

        start_message = '''Jocul a inceput! Ai fost pus intr-un labirint intunecat.
Pentru a evada poti merge doar in sus (U), in jos (D), in stanga (L)
sau in dreapta (R). Introdu caracterul pentru mutarea ta!'''
        serverSocket.sendto(start_message.encode(), clientAddr)

        # Se alege un model de labirint
        maze = pick_maze()
        print("Alegerea labirintului:")
        print_maze(maze)

        # Se alege random pozitia jucatorului
        player_pos = pick_player_position(maze)
        maze[player_pos[0]][player_pos[1]] = "J"
        print("Labirintul dupa pozitionarea jucatorului:")
        print_maze(maze)

        # Se alege random pozitia monstrului
        monster_pos = pick_monster_position(maze)
        maze[monster_pos[0]][monster_pos[1]] = "M"

        finish_pos = get_finish_pos(maze)

        print("Dupa alegerea pozitiei jucatorului si monstrului vom avea labirintul urmator:")
        print_maze(maze)

        number_moves = 0
        move, clientAddr = serverSocket.recvfrom(300)
        result, player_pos = do_player_move(maze, move.decode(), player_pos)
        while result == 0 or result == -1:
            print_maze(maze)
            if result == -1:
                wall_message = 'WALL'
                serverSocket.sendto(wall_message.encode(), clientAddr)
            else:
                number_moves += 1
                ok_message = 'OK'
                serverSocket.sendto(ok_message.encode(), clientAddr)
            move, clientAddr = serverSocket.recvfrom(300)
            result, player_pos = do_player_move(maze, move.decode(), player_pos)

        if result == 1:
            winning_message = 'WIN'
            serverSocket.sendto(winning_message.encode(), clientAddr)
            serverSocket.sendto(str(number_moves).encode(), clientAddr)
        
        elif result == -2:
            lose_message = 'MONSTER'
            serverSocket.sendto(lose_message.encode(), clientAddr)

        message, clientAddr = serverSocket.recvfrom(300)

    # Conditia de terminare a jocului
    if message.decode() == 'STOP':
        # print("am intrat pe iful de stop")
        break
print("Joc terminat")
