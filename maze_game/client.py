import socket

def get_move():
    accepted_moves = ['U', 'D', 'L', 'R']
    move = input()
    while move not in accepted_moves:
        print("Input incorect! Foloseste doar U, D, L sau R!")
        move = input()
    return move

serverAddr = '127.0.0.1'
serverPort = 30000

clientSocket = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)

print("Bine ai venit in jocul 'Evadarea din labirint'!")
start = input("Pentru a incepe, introduceti mesajul 'START'!\n")
while start != 'START':
    print("Mesaj incorect! pentru a incepe scrie 'START'!\n")
    start = input()
clientSocket.sendto(start.encode(), (serverAddr, serverPort))

while start == 'START':
    # Primirea si afisarea mesajului de start joc
    start_message, servAddr = clientSocket.recvfrom(300)
    print(start_message.decode())

    move = get_move()
    clientSocket.sendto(move.encode(), (serverAddr, serverPort))
    message, servAddr = clientSocket.recvfrom(300)
    while message.decode() == 'OK' or message.decode() == 'WALL':
        if message.decode() == 'OK':
            print("OK!")
        else:
            print("Imposibil, ai lovit un perete. Incearca alta directie.")
        move = get_move()
        clientSocket.sendto(move.encode(), (serverAddr, serverPort))
        message, servAddr = clientSocket.recvfrom(300)
    if message.decode() == 'WIN':
        number_moves, servAddr = clientSocket.recvfrom(300)
        number_moves = int (number_moves.decode())
        print(f"Ai reusit! Ai iesit din labirint din {number_moves} miscari!")
    elif message.decode() == 'MONSTER':
        print("Ai picat prada monstrului din labirint :(... ai pierdut jocul. Incearca din nou!")

    start = input("START pentru a juca din nou / STOP pentru a incheia\n")
    while start != "START" and start != "STOP":
        start = input ("Input incorect!\n")
    clientSocket.sendto(start.encode(), (serverAddr, serverPort))


print('Multumim ca ai jucat! Speram sa revii :) !')
clientSocket.close()