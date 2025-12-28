import { Component, OnDestroy, OnInit } from "@angular/core";
import { Subscription } from "rxjs";
import { AuthService } from "./auth.service";
import { ChatService } from "./chat.service";
import { ChatMessage, ChatRoom, UserProfile } from "./models";

@Component({
  selector: "app-root",
  templateUrl: "./app.component.html",
  styleUrls: ["./app.component.css"]
})
export class AppComponent implements OnInit, OnDestroy {
  rooms: ChatRoom[] = [];
  selectedRoom: ChatRoom | null = null;
  messages: ChatMessage[] = [];
  newRoomName = "";
  newMessage = "";
  user: UserProfile | null = null;
  connected = false;

  private readonly subscriptions: Subscription[] = [];

  constructor(private chatService: ChatService, private authService: AuthService) {}

  ngOnInit(): void {
    this.chatService.connect();

    this.subscriptions.push(
      this.chatService.connected$.subscribe((connected) => {
        this.connected = connected;
      })
    );

    this.subscriptions.push(
      this.chatService.messages$.subscribe((message) => {
        if (this.selectedRoom && message.roomId === this.selectedRoom.id) {
          this.messages = [...this.messages, message];
        }
      })
    );

    this.subscriptions.push(
      this.authService.user$.subscribe((user) => {
        this.user = user;
      })
    );

    this.authService.loadProfile().subscribe();
    this.refreshRooms();
  }

  refreshRooms(): void {
    this.chatService.listRooms().subscribe({
      next: (rooms) => {
        this.rooms = rooms;
        if (!this.selectedRoom && rooms.length > 0) {
          this.selectRoom(rooms[0]);
        }
      },
      error: () => {
        this.rooms = [];
      }
    });
  }

  selectRoom(room: ChatRoom): void {
    this.selectedRoom = room;
    this.messages = [];
    this.chatService.subscribeToRoom(room.id);
    this.chatService.getMessages(room.id).subscribe({
      next: (messages) => {
        this.messages = messages;
      },
      error: () => {
        this.messages = [];
      }
    });
  }

  createRoom(): void {
    const name = this.newRoomName.trim();
    if (!name) {
      return;
    }

    this.chatService.createRoom(name).subscribe({
      next: (room) => {
        this.newRoomName = "";
        this.rooms = [...this.rooms, room];
        this.selectRoom(room);
      }
    });
  }

  sendMessage(): void {
    if (!this.selectedRoom) {
      return;
    }

    const content = this.newMessage.trim();
    if (!content) {
      return;
    }

    const sender = this.user?.displayName || this.user?.username || "anonymous";
    this.chatService.sendMessage(this.selectedRoom.id, sender, content);
    this.newMessage = "";
  }

  login(): void {
    this.authService.login();
  }

  logout(): void {
    this.authService.logout();
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
    this.chatService.disconnect();
  }
}
